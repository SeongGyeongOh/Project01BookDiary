package com.osg.project01bookdiary_sharedreview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.osg.project01bookdiary.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<RecyclerCommentItem> items;

    public CommentAdapter(Context context, ArrayList<RecyclerCommentItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_comment, parent, false);

        VH viewHolder = new VH(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH viewHolder=(VH)holder;
        RecyclerCommentItem item = items.get(position);

        viewHolder.tvNickname.setText(item.nickName);
        viewHolder.tvComment.setText(item.comment);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tvComment, tvNickname;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvNickname = itemView.findViewById(R.id.tv_commentName);
        }
    }
}
