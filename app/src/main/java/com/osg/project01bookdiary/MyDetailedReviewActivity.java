package com.osg.project01bookdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MyDetailedReviewActivity extends AppCompatActivity {

    ImageView ivCover;
    TextView tvTitle, tvAuthor, tvReview, tvReviewTitle;

    String title;
    String author;
    String image;
    String reviewTitle;
    String reveiw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detailed_review);

        ivCover = findViewById(R.id.coveriv);
        tvTitle = findViewById(R.id.titletv);
        tvAuthor = findViewById(R.id.authortv);
        tvReviewTitle = findViewById(R.id.reviewTitletv);
        tvReview = findViewById(R.id.reviewtv);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");
        author = bundle.getString("author");
        image = bundle.getString("cover");
        reviewTitle = bundle.getString("reviewtitle");
        reveiw = bundle.getString("review");


        Glide.with(this).load(image).into(ivCover);
        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvReviewTitle.setText(reviewTitle);
        tvReview.setText(reveiw);

    }

    public void clickFloat(View view) {
        // 리뷰 작성 액티비티로 넘어가서 데이터 변경하기
        Intent intent = new Intent(this, ReviseRevActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        intent.putExtra("cover", image);
        intent.putExtra("reviewtitle", reviewTitle);
        intent.putExtra("review", reveiw);


        startActivity(intent);

    }

}