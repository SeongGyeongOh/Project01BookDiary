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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osg.project01bookdiary.G;
import com.osg.project01bookdiary.R;
import com.osg.project01bookdiary.RetrofitHelper;
import com.osg.project01bookdiary.RetrofitService;

import java.sql.Time;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
    ArrayList<EventDay> events=new ArrayList<>();
    int y, m, d;
    Calendar clickedDay;
    DatabaseReference ref;
    View view;

    RecyclerView recyclerView;
    ArrayList<MemoItem> items=new ArrayList<>();
    RecyclerMemoAdapter adapter;

    ArrayList<Calendar> calendars=new ArrayList<>();

    static ChildEventListener listener;

    int year, month, date, hour, minute, second;

    int i=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_calendar, container, false);

        calendarView=view.findViewById(R.id.calendar);
        tvNote=view.findViewById(R.id.tv_note);
        events = new ArrayList<>();
        recyclerView=view.findViewById(R.id.recycle);

        adapter=new RecyclerMemoAdapter(getContext(), items, events);

        recyclerView.setAdapter(adapter);

        listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MemoItem item=snapshot.getValue(MemoItem.class);
                items.add(item);

                Calendar calendar=Calendar.getInstance();
                calendar.set(item.year, item.month-1, item.date);
                events.add(new EventDay(calendar, R.drawable.ic_baseline_menu_book_24));
                calendarView.setEvents(events);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        //달력 날짜를 클릭했을 때
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Log.i("DATE", ""+year+month);
                clickedDay=eventDay.getCalendar();
                y=clickedDay.get(Calendar.YEAR);
                m=clickedDay.get(Calendar.MONTH)+1;
                d=clickedDay.get(Calendar.DATE);

                calendars.add(0,clickedDay);


                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View v = getLayoutInflater().inflate(R.layout.alertdialog_calendar, null);
                etMemo=v.findViewById(R.id.et_memo);
                builder.setView(v).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(etMemo.getText()!=null){
                            String memo=etMemo.getText().toString();

                            //메모한 내용을 Firebase에 저장(메모한 날짜+메모 내용)
                            MemoItem memoItem=new MemoItem(y, m, d, memo);
                            FirebaseDatabase db=FirebaseDatabase.getInstance();
                            DatabaseReference ref1=db.getReference("Calendar"+G.nickName).child(""+y+m).child(""+m+d);
                            ref1.setValue(memoItem);
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

            }
        });

        //전 달 클릭
        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                showMemo();
            }
        });

        //다음달 클릭
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                showMemo();
            }
        });

        showMemo();
        return view;
    }

    public void showMemo(){
        getDate();

        tvNote.setText(month+"월 독서 일정");

        items.clear();
        adapter.notifyDataSetChanged();

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        ref=db.getReference("Calendar"+G.nickName).child(""+year+month);
        ref.removeEventListener(listener);
        ref.addChildEventListener(listener);

        adapter.notifyDataSetChanged();
    }

    public void pushData(){
        //TODO: 특정 시간에!! 서버로 데이터가 날라가며 push 알람이 뜨도록 설정하기!!


//        ArrayList<Calendar> dates=(ArrayList)calendarView.getSelectedDates();
//
//        Calendar now=dates.get(i);
//        GregorianCalendar gre=new GregorianCalendar(year,month,date,11,11,0);
//        now.getTime().getTime();
//        Toast.makeText(getContext(), ""+now.getTime(), Toast.LENGTH_SHORT).show();
//        Log.i("CAL", ""+now.getTime().getTime());
//        Log.i("GRE",""+gre.getTime());
//
//        if(now.getTime()==gre.getTime()){
//            Retrofit retrofit= RetrofitHelper.getString();
//            RetrofitService retrofitService=retrofit.create(RetrofitService.class);
//            Call<String> call=retrofitService.uploadPushData(""+d+"의 독서 목표", etMemo.getText().toString(), G.token);
//            call.enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    if(response.isSuccessful()){
//                        Toast.makeText(getContext(), response.body()+"", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                }
//            });
//        }
//        i++;
    }

    public void getDate(){
        Calendar calendar=calendarView.getCurrentPageDate();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        date=calendar.get(Calendar.DATE);
        hour=calendar.get(Calendar.HOUR);
        minute=calendar.get(Calendar.MINUTE);
        second=calendar.get(Calendar.SECOND);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        ref=db.getReference("Calendar"+G.nickName).child(""+year+month);
        ref.removeEventListener(listener);
        adapter.notifyDataSetChanged();
    }
}


