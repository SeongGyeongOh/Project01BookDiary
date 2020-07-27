package com.osg.project01bookdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditRevActivity extends AppCompatActivity {

    ImageView ivCover;
    TextView tvTitle, tvAuthor;
    EditText etReviewTitle, etReview;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rev);

        ivCover = findViewById(R.id.coveriv);
        tvTitle = findViewById(R.id.titletv);
        tvAuthor = findViewById(R.id.authortv);
        etReviewTitle = findViewById(R.id.reviewTitleet);
        etReview = findViewById(R.id.reviewet);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String image = bundle.getString("cover");
        title = bundle.getString("title");
        String author = bundle.getString("author");
        String reviewTitle = bundle.getString("reviewtitle");
        String reveiw = bundle.getString("review");

        Glide.with(this).load(image).into(ivCover);
        tvTitle.setText(title);
        tvAuthor.setText(author);
        etReviewTitle.setText(reviewTitle);
        etReview.setText(reveiw);
    }

    public void clickRev(View view) {
        //리뷰 제목과 컨텐츠를 DB에 업데이트 하기
        Retrofit retrofit = RetrofitHelper.getString();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<String> call = retrofitService.updateDBdate(G.nickName, etReviewTitle.getText().toString(), etReview.getText().toString(), title);

        InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etReview.getWindowToken(),0);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText(EditRevActivity.this, response.body()+"", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) { }
        });
        finish();
    }
}