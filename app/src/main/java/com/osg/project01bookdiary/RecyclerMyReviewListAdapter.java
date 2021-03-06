package com.osg.project01bookdiary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.osg.project01bookdiary_settings.Fragment05Settings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

//Tab01MyReviewFragment 아답터
public class RecyclerMyReviewListAdapter extends RecyclerView.Adapter {

    ArrayList<Tab01myreview_item> items;
    Context context;

    String imageUrl, nickName;

    public RecyclerMyReviewListAdapter(ArrayList<Tab01myreview_item> items, Context context) {
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
            btn = itemView.findViewById(R.id.btn);
            btnDel=itemView.findViewById(R.id.btnDel);

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

            //공유 버튼
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickBtn();
                }
            });

            //삭제 버튼
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int num = items.get(getLayoutPosition()).no;
                            String tableName = G.nickName;

                            Retrofit retrofit = RetrofitHelper.getString();
                            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                            Call<String> call = retrofitService.deleteDataFromDB(tableName, num);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(context, response.body()+"", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {}
                            });
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

        void onClickBtn(){
            new AlertDialog.Builder(context).setMessage("공유 하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //데이터들 얻어와서 SharedReview Table로 전송
                    String ID = G.nickName;
                    loadProfileData();

                    String profileImage=null;
                    String profileName;
                    String bookCover;
                    if(imageUrl!=null) profileImage=imageUrl;
                    else if(imageUrl==null&&G.loginProfileImg!=null) profileImage=G.loginProfileImg;
                    else if(imageUrl==null&&G.loginProfileImg==null) profileImage="https://firebasestorage.googleapis.com/v0/b/fir-cloudstore-8c242.appspot.com/o/icon-playstore.png?alt=media&token=a2a55f25-45de-4682-9f6a-ae7a307d30d9";

                    if(nickName==null) profileName=G.loginProfileName;
                    else profileName=nickName;

                    if(items.get(getLayoutPosition()).image.isEmpty()){
                        bookCover="https://firebasestorage.googleapis.com/v0/b/fir-cloudstore-8c242.appspot.com/o/noimage.png?alt=media&token=74cfc1c7-189c-461b-8f9d-fe35e64521ad";
                    }else{
                        bookCover=items.get(getLayoutPosition()).image;
                    }
                    String bookTitle = items.get(getLayoutPosition()).bookTitle;
                    String bookAuthor = items.get(getLayoutPosition()).bookAuthor;
                    String reviewTitle = items.get(getLayoutPosition()).reviewTitle;
                    String reviewContent = items.get(getLayoutPosition()).reviewContent;

                    SharedItem sharedItem = new SharedItem(ID, profileImage, profileName, bookCover, bookTitle, bookAuthor, reviewTitle, reviewContent);
                    Retrofit retrofit = RetrofitHelper.getJsonFromDB();
                    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                    Call<String> call = retrofitService.updateSharedReview(sharedItem);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) { }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {}
                    });
                    Toast.makeText(context, "공유됐습니다", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            }).show();
        }

    }

    void loadProfileData(){
        SharedPreferences sharedPreferences =context.getSharedPreferences("Profile"+G.nickName, MODE_PRIVATE);
        imageUrl=sharedPreferences.getString("Setting Profile Image", null);
        nickName=sharedPreferences.getString("Setting Profile Name", null);
    }

}
