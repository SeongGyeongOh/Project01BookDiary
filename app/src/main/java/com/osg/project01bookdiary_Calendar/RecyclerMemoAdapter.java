package com.osg.project01bookdiary_Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.osg.project01bookdiary.R;

import java.util.ArrayList;

public class RecyclerMemoAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MemoItem> items;

    public RecyclerMemoAdapter(Context context, ArrayList<MemoItem> items) {
        this.context = context;
        this.items = items;
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
        }
    }
}

