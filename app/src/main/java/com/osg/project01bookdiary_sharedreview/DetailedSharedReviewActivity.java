package com.osg.project01bookdiary_sharedreview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osg.project01bookdiary.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailedSharedReviewActivity extends AppCompatActivity {

    ImageView bookCoverImg;
    TextView bookName, bookAuthorName;
    CircleImageView profileImg;
    TextView profileName;
    TextView reviewContent;

    EditText etComment;
    Button btnSubmit;
    RecyclerView recyclerView;
    ArrayList<RecyclerCommentItem> items = new ArrayList<>();
    CommentAdapter adapter;

    DatabaseReference rootRef;
    DatabaseReference commentRef;

    int num;

    String bookCover, bookTitle, profileImage, profileNickName, bookAuthor, review;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_shared_review);

        bookCoverImg = findViewById(R.id.iv_dsBookCover);
        bookName = findViewById(R.id.tv_dsBookTitle);
        bookName.setSelected(true);
        bookAuthorName = findViewById(R.id.tv_dsBookAuthor);
        profileImg = findViewById(R.id.iv_dsProfileImg);
        profileName = findViewById(R.id.tv_dsProfileName);
        reviewContent = findViewById(R.id.tv_dsReview);
        etComment = findViewById(R.id.et_dsComment );
//        btnSubmit = findViewById(R.id.btn_dsBtn);

        Intent intent = getIntent();
        Bundle datas = intent.getExtras();
        bookCover = datas.getString("bookCover");
        bookTitle = datas.getString("bookTitle");
        profileImage = datas.getString("profileImg");
        profileNickName = datas.getString("profileName");
        bookAuthor = datas.getString("bookAuthor");
        review = datas.getString("review");
        num = datas.getInt("no");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        commentRef = firebaseDatabase.getReference("comment"+num);

        recyclerView=findViewById(R.id.recycler_ds);

        // 책 정보 + 리뷰 보이기
        showReview();

        //리사이클러뷰로 코멘트 데이터들 보여주기
        showComment();

        adapter = new CommentAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    public void clickSaveComment(View view) {
        //코멘트 저장 버튼
        text = etComment.getText().toString();
        if(text.isEmpty()) return;

        RecyclerCommentItem recyclerCommentItem = new RecyclerCommentItem(profileNickName, text);
        commentRef.push().setValue(recyclerCommentItem);

        etComment.setText("");
    }

    public void showReview(){
        //책 정보-리뷰
        Glide.with(this).load(bookCover).into(bookCoverImg);
        bookName.setText(bookTitle);
        Glide.with(this).load(profileImage).into(profileImg);
        profileName.setText(profileNickName);
        bookAuthorName.setText(bookAuthor);
        reviewContent.setText(review);
    }

    public void showComment(){
        //코멘트 보이기기
        commentRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RecyclerCommentItem item = snapshot.getValue(RecyclerCommentItem.class);

                items.add(0, item);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}