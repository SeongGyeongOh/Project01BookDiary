package com.osg.project01bookdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Fragment01MyPage extends Fragment {

    TabLayout tabLayout;
    ViewPager pager;
    FragmentPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_mypage, container, false);

        tabLayout = view.findViewById(R.id.tablayout);
        pager = view.findViewById(R.id.pager);
        adapter = new FragmentPagerAdapter( getActivity().getSupportFragmentManager());

        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);

        return view;
    }
}
