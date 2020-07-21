package com.osg.project01bookdiary_Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.osg.project01bookdiary.G;
import com.osg.project01bookdiary.R;
import com.osg.project01bookdiary.RetrofitHelper;
import com.osg.project01bookdiary.RetrofitService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Fragment04Calendar extends Fragment {

    CalendarView calendarView;
    EditText etMemo;
    TextView tvNote;

    ArrayList<EventDay> events;

    int y, m, d;

    Calendar clickedDay;

    SQLiteDatabase db;
    String dbName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_calendar, container, false);

        calendarView=view.findViewById(R.id.calendar);
        tvNote=view.findViewById(R.id.tv_note);
        events = new ArrayList<>();

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                clickedDay=eventDay.getCalendar();

                y=clickedDay.get(Calendar.YEAR);
                m=clickedDay.get(Calendar.MONTH);
                d=clickedDay.get(Calendar.DATE);

                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View v = getLayoutInflater().inflate(R.layout.alertdialog_calendar, null);
                etMemo=v.findViewById(R.id.et_memo);
                builder.setView(v);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(etMemo.getText()!=null){
                            //달력에 메모한 내용을 먼저 SharedPreferences에 저장(메모한 날짜+메모 내용)
                            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Memo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            String memo=etMemo.getText().toString();
                            editor.putString("memo", memo);
                            editor.putInt("year", y);
                            editor.putInt("month"+m, m);
                            editor.putInt("date", d);
                            editor.commit();

//                            db=SQLiteDatabase.openOrCreateDatabase(dbName,null);
//
//                            db.execSQL("CREATE TABLE IF NOT EXISTS "+G.nickName+"(memo text not null, year integer, month integer, date integer)");
//                            db.execSQL("INSERT INTO "+G.nickName+"(memo, year, month, date) VALUES('"+memo+"','"+y+"','"+m+"','"+d+"')");

                        }
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //TODO: 특정 시간에 서버로 데이터가 날라가며 push 알람이 뜨도록 설정하기!!
                //TODO: 선택한 날짜+해당 날짜의 일정을 SharedPreference에 저장해서..저장하기
                if(System.currentTimeMillis()==eventDay.getCalendar().getTimeInMillis()){
                    Retrofit retrofit= RetrofitHelper.getString();
                    RetrofitService retrofitService=retrofit.create(RetrofitService.class);
                    Call<String> call=retrofitService.uploadPushData(""+d+"의 독서 목표", etMemo.getText().toString(), G.token);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getContext(), response.body()+"", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });

        //저장한 메모 내용을 보여주는 메소드
        showMemo();

        return view;
    }

    void showMemo(){
        SharedPreferences pref=getActivity().getSharedPreferences("Memo", Context.MODE_PRIVATE);
        String note=pref.getString("memo", "");
        int year= pref.getInt("year",0);
        int month=pref.getInt("month", 0);
        int day=pref.getInt("date", 0);

        Toast.makeText(getContext(), note+year+month+day+"", Toast.LENGTH_SHORT).show();

        Calendar cal=Calendar.getInstance();
        cal.set(year, month, day);

        tvNote.append("\n"+day+"일 : "+note+"\n");
        events.add(new EventDay(cal, R.drawable.ic_baseline_menu_book_24));

        calendarView.setEvents(events);


    }
}
