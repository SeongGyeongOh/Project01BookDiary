package com.osg.project01bookdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter {

    ArrayList<Tab01myreview_item> items;
    Context context;

    public RecyclerAdapter(ArrayList<Tab01myreview_item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_myreview, parent, false);
        VH viewHolder = new VH(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH viewHolder = (VH)holder;
        Tab01myreview_item item =items.get(position);

        if(item.image.isEmpty()){
            viewHolder.cover.setImageResource(R.drawable.noimage);
        }else{
            Glide.with(context).load(item.image).into(viewHolder.cover);
        }

        viewHolder.title.setText(item.bookTitle);
        viewHolder.revTitle.setText(item.reviewTitle);
        viewHolder.revContent.setText(item.reviewContent);
//        viewHolder.date.setText(item.);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView title;
        TextView revTitle;
        TextView revContent;
        TextView date;
        Button btn;

        public VH(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.bookCover);
            title = itemView.findViewById(R.id.book_title);
            revTitle =itemView.findViewById(R.id.rev_title);
            revContent = itemView.findViewById(R.id.rev_content);
//            date = itemView.findViewById(R.id.tv_date);
            btn = itemView.findViewById(R.id.btn);

        }
    }
}
