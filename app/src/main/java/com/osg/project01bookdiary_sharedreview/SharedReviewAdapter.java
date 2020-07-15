package com.osg.project01bookdiary_sharedreview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.osg.project01bookdiary.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedReviewAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<SharedReview_item> items;

    public SharedReviewAdapter(Context context, ArrayList<SharedReview_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_sharedreview, parent, false);
        VH viewHolder = new VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH viewHolder = (VH)holder;
        SharedReview_item item = items.get(position);

        Glide.with(context).load(item.profileImage).into(viewHolder.ivProfile);
        viewHolder.tvProfile.setText(item.profileName);
        viewHolder.tvBookTitle.setText(item.bookTitle+"  "+item.bookAuthor);
        viewHolder.tvReviewTitle.setText(item.reviewTitle);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        CircleImageView ivProfile;
        TextView tvProfile, tvBookTitle, tvReviewTitle;

        public VH(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.profileIV);
            tvProfile = itemView.findViewById(R.id.profileTV);
            tvBookTitle = itemView.findViewById(R.id.bookTV);
            tvReviewTitle = itemView.findViewById(R.id.revtitleTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailedSharedReviewActivity.class);
                    intent.putExtra("bookCover", items.get(getLayoutPosition()).bookCover);
                    intent.putExtra("bookTitle", items.get(getLayoutPosition()).bookTitle);
                    intent.putExtra("bookAuthor", items.get(getLayoutPosition()).bookAuthor);

                    intent.putExtra("profileImg", items.get(getLayoutPosition()).profileImage);
                    intent.putExtra("profileName", items.get(getLayoutPosition()).profileName);

                    intent.putExtra("review", items.get(getLayoutPosition()).reviewTitle +"\n\n"+items.get(getLayoutPosition()).reviewContent);
                    intent.putExtra("no", items.get(getLayoutPosition()).no);
                    context.startActivity(intent);
                }
            });
        }
    }
}


