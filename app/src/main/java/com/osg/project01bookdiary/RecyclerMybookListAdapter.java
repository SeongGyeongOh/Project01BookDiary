package com.osg.project01bookdiary;

import android.content.Context;
import android.content.Intent;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.view.View.GONE;

public class RecyclerMybookListAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Tab01myreview_item> items;

    public RecyclerMybookListAdapter(Context context, ArrayList<Tab01myreview_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_mybooklist_item, parent, false);
        VH viewHolder = new VH(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH viewHolder = (VH)holder;
        Tab01myreview_item item = items.get(position);

        Glide.with(context).load(item.image).into(viewHolder.iv);
//        viewHolder.ratingBar.setNumStars(item.rating==1?1:2);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        ImageView iv;
//        RatingBar ratingBar;

        public VH(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv_book);
//            ratingBar = itemView.findViewById(R.id.ratingbar);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, AddRevActivity.class);
//                    intent.putExtra("title", items.get(getLayoutPosition()).bookTitle);
//                    intent.putExtra("image", items.get(getLayoutPosition()).image);
//                    intent.putExtra("author", items.get(getLayoutPosition()).bookAuthor);
//                    intent.putExtra("review", items.get(getLayoutPosition()).reviewTitle+"\n \n"+items.get(getLayoutPosition()).reviewContent);
//                    context.startActivity(intent);
//                }
//            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    view.setVisibility(GONE);
                    return true;
                }
            });
        }

    }
}
