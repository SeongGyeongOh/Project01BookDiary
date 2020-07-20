package com.osg.project01bookdiary_Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                Calendar clickedDay=eventDay.getCalendar();

                int y=clickedDay.get(Calendar.YEAR);
                int m=clickedDay.get(Calendar.MONTH);
                int d=clickedDay.get(Calendar.DATE);

                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View v = getLayoutInflater().inflate(R.layout.alertdialog_calendar, null);
                etMemo=v.findViewById(R.id.et_memo);
                builder.setView(v);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(etMemo.getText()!=null){
                            tvNote.append(d+"일 : "+etMemo.getText().toString()+"\n");

                            events.add(new EventDay(clickedDay, R.drawable.ic_baseline_menu_book_24));
                        }
                        calendarView.setEvents(events);

//                        Date date=new Date();
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



        return view;
    }
}
