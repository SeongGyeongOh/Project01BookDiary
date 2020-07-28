package com.osg.project01bookdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.osg.project01bookdiary_Calendar.Fragment04Calendar;
import com.osg.project01bookdiary_sharedreview.Fragment03SharedReview;
import com.osg.project01bookdiary_settings.Fragment05Settings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    //https://app-privacy-policy-generator.firebaseapp.com/ 개인정보처리방침 생성 링크!!

    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;

    Fragment[] fragments = new Fragment[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toast.makeText(this, G.loginProfileName+G.loginProfileImg+"", Toast.LENGTH_SHORT).show();

        //달력+푸시에 쓸 토큰 얻어오기
        getToken();

        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navi);

        fragments[0] =new Fragment01MyPage();
        fragments[1] =new Fragment02SearchBook();
        fragments[2] = new Fragment03SharedReview();
        fragments[3] = new Fragment04Calendar();
        fragments[4] = new Fragment05Settings();

        FragmentTransaction tran = fragmentManager.beginTransaction();
        tran.add(R.id.container, fragments[0]);
        tran.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentTransaction tran = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.mypage:
                        tran.replace(R.id.container, fragments[0]);
                        break;

                    case R.id.search:
                        tran.replace(R.id.container, fragments[1]);
                        break;

                    case R.id.shared:
                        tran.replace(R.id.container, fragments[2]);
                        break;

                    case R.id.calendar:
                        tran.replace(R.id.container, fragments[3]);
                        break;

                    case R.id.settings:
                        tran.replace(R.id.container, fragments[4]);
                        break;
                }
                tran.commit();
                return true;
            }
        });
    }

    void getToken(){
        FirebaseInstanceId firebaseInstanceId=FirebaseInstanceId.getInstance();
        Task<InstanceIdResult> task=firebaseInstanceId.getInstanceId();
        task.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                G.token=task.getResult().getToken();
                Log.i("TOKEN", G.token);
            }
        });
    }
}