package com.osg.project01bookdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;

    Fragment[] fragments = new Fragment[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
}