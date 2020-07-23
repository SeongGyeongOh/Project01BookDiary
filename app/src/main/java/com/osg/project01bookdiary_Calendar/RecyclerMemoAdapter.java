package com.osg.project01bookdiary_Calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osg.project01bookdiary.G;
import com.osg.project01bookdiary.R;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerMemoAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MemoItem> items;
    ArrayList<EventDay> events;

    public RecyclerMemoAdapter(Context context, ArrayList<MemoItem> items, ArrayList<EventDay> events) {
        this.context = context;
        this.items = items;
        this.events=events;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_memo_item, parent, false);
        VH viewHolder=new VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH viewHoler=(VH)holder;
        MemoItem item=items.get(position);

        viewHoler.tvMemo.setText(item.date+"일 "+item.memo);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        RadioButton radioButton;
        TextView tvMemo;

        public VH(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.radio);
            tvMemo=itemView.findViewById(R.id.tvMemo);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context).setNeutralButton(items.get(getLayoutPosition()).date+"일 일정 삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase db=FirebaseDatabase.getInstance();
                            DatabaseReference ref=db.getReference().child("Calendar"+ G.nickName)
                                    .child(""+items.get(getLayoutPosition()).year+items.get(getLayoutPosition()).month)
                                    .child(""+items.get(getLayoutPosition()).month+items.get(getLayoutPosition()).date);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue();

                                    Calendar calendar=Calendar.getInstance();
                                    calendar.set(items.get(getLayoutPosition()).year, items.get(getLayoutPosition()).month, items.get(getLayoutPosition()).date);
                                    events.remove(calendar);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                        }
                    }).show();
                    return true;
                }
            });
        }
    }
}

