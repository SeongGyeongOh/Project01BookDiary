package com.osg.project01bookdiary_sharedreview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.EventDay;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osg.project01bookdiary.G;
import com.osg.project01bookdiary.R;
import com.osg.project01bookdiary.RetrofitHelper;
import com.osg.project01bookdiary.RetrofitService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        View view2=LayoutInflater.from(context).inflate(R.layout.recycler_sharedreview_my, parent, false);

        VHSecond viewHolder = new VHSecond(view2);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VHSecond viewHolder = (VHSecond) holder;
        SharedReview_item item = items.get(position);

            Glide.with(context).load(item.profileImage).into(viewHolder.ivProfiles);
            viewHolder.tvProfile.setText(item.profileName);
            viewHolder.tvProfile.setText(item.profileName);
            viewHolder.tvBookTitle.setText(item.bookTitle+"  "+item.bookAuthor);
            viewHolder.tvReviewTitle.setText(item.reviewTitle);

            if(G.nickName.equals(item.ID)){
                viewHolder.tvDeleteRev.setVisibility(View.VISIBLE);
            }else{
                viewHolder.tvDeleteRev.setVisibility(View.GONE);
            }
        }
        @Override
    public int getItemCount() {
        return items.size();
    }

    class VHSecond extends RecyclerView.ViewHolder{
        CircleImageView ivProfiles;
        TextView tvProfile, tvBookTitle, tvReviewTitle, tvDeleteRev;

        public VHSecond(@NonNull View itemView) {
            super(itemView);

            ivProfiles=itemView.findViewById(R.id.profilesIV);
            tvProfile = itemView.findViewById(R.id.profileTV);
            tvBookTitle = itemView.findViewById(R.id.bookTV);
            tvReviewTitle = itemView.findViewById(R.id.revtitleTV);
            tvDeleteRev=itemView.findViewById(R.id.tv_delete);
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

            //공유된 리뷰 삭제하기
            tvDeleteRev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context).setMessage("공유한 기록을 삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //DB에 있는 기록 삭제하는 코드 작성하기
                            Retrofit retrofit= RetrofitHelper.getString();
                            RetrofitService service=retrofit.create(RetrofitService.class);
                            Call<String> call=service.deleteMySharedReview(items.get(getLayoutPosition()).no);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(context, response.body()+"", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {   }
                            });

                            //해당 리뷰의 코멘트도 Firebase에서 삭제하기
                            FirebaseDatabase db=FirebaseDatabase.getInstance();
                            DatabaseReference ref=db.getReference().child("comment").child("comment"+items.get(getLayoutPosition()).no);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    builder.show();
                }
            });
        }
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


