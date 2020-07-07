package com.osg.project01bookdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerSearchBookAdapter extends RecyclerView.Adapter {

    ArrayList<Document> items;
    Context context;

    public RecyclerSearchBookAdapter(ArrayList<Document> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_searchbook, parent, false);
        VH viewHolder = new VH(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH)holder;
        Document item = items.get(position);

        Glide.with(context).load(item.thumbnail).into(vh.iv);
        vh.tvTitle.setText(item.title);
        ArrayList<String> authors = item.authors;
        for(String author : authors){
            vh.tvAuthor.setText(author + " ");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvTitle;
        TextView tvAuthor;

        public VH(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.ivTitle);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
