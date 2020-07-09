package com.osg.project01bookdiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddRevActivity extends AppCompatActivity {

    ImageView iv;
    TextView tvTitle;
    TextView tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rev);

        iv = findViewById(R.id.iv_cover);
        tvTitle = findViewById(R.id.bookTitle);
        tvAuthor = findViewById(R.id.bookAuthor);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String image = bundle.getString("image");
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


    }

}