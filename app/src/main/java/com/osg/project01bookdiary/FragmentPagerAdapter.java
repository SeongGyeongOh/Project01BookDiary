package com.osg.project01bookdiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

    Fragment[] fragments = new Fragment[2];
    String[] tabTexts = new String[]{"나의 기록", "나의 도서 목록"};

    public FragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);

        fragments[0] = new Tab01MyReviewFragment();
        fragments[1] = new Tab02MyBookListFragment();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTexts[position];
    }
}
