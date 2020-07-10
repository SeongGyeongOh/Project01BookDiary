package com.osg.project01bookdiary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

        viewHolder.title.setText(item.bookTitle +"\n"+item.bookAuthor);
        viewHolder.revTitle.setText(item.reviewTitle);
        viewHolder.revContent.setText(item.reviewContent);
        viewHolder.date.setText(item.date);
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
        Button btnDel;

        public VH(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.bookCover);
            title = itemView.findViewById(R.id.book_title);
            revTitle =itemView.findViewById(R.id.rev_title);
            revContent = itemView.findViewById(R.id.rev_content);
            date = itemView.findViewById(R.id.tv_date);
            btnDel=itemView.findViewById(R.id.btnDel);

//            date = itemView.findViewById(R.id.tv_date);
            btn = itemView.findViewById(R.id.btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MyDetailedReviewActivity.class);
                    intent.putExtra("title", items.get(getLayoutPosition()).bookTitle);
                    intent.putExtra("cover", items.get(getLayoutPosition()).image);
                    intent.putExtra("author", items.get(getLayoutPosition()).bookAuthor);
                    intent.putExtra("reviewtitle", items.get(getLayoutPosition()).reviewTitle);
                    intent.putExtra("review", items.get(getLayoutPosition()).reviewContent);
                    context.startActivity(intent);
                }
            });

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });

                    AlertDialog alertDialog = builder.show();
                }
            });

        }
    }
}
