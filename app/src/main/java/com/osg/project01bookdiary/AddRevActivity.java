package com.osg.project01bookdiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddRevActivity extends AppCompatActivity {

    ImageView iv;
    TextView tvTitle;
    TextView tvAuthor;

    EditText etTitle;
    TextInputEditText etContent;

    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rev);

        iv = findViewById(R.id.iv_cover);
        tvTitle = findViewById(R.id.bookTitle);
        tvAuthor = findViewById(R.id.bookAuthor);


        etTitle = findViewById(R.id.et_revTitle);
        etContent = findViewById(R.id.et_revContent);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        image = bundle.getString("image");
        String title = bundle.getString("title");
        String author = bundle.getString("author");

        if(image.isEmpty()){
            iv.setImageResource(R.drawable.noimage);
        }else {
            Glide.with(this).load(image).into(iv);
        }
        tvTitle.setText(title);
        tvAuthor.setText(author);

    }

    public void clickBack(View view) {
        finish();
    }

    public void clickSave(View view) {
        //책 정보, 리뷰 내용 DB에 전송
        String bookTitle = tvTitle.getText().toString();
        String bookAuthor = tvAuthor.getText().toString();
        String revTitle = etTitle.getText().toString();
        String revContent = etContent.getText().toString();

        String img = image;

        //데이터 서버에 보내기
        Retrofit retrofit = RetrofitHelper.getString();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<String> call = retrofitService.postReviewData(G.nickName, img, bookTitle, bookAuthor, revTitle, revContent);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AddRevActivity.this, t.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(AddRevActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}