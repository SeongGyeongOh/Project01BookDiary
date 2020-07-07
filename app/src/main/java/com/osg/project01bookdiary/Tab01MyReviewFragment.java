package com.osg.project01bookdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Tab01MyReviewFragment extends Fragment {

    ArrayList<Tab01myreview_item> items = new ArrayList<>();
    RecyclerAdapter myAdapter;
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab01_layout, container, false);

        items.add(new Tab01myreview_item(null, "책 제목", "리뷰 내용", "20200401"));
        myAdapter = new RecyclerAdapter(items, getContext());
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(myAdapter);


        return view;

    }
}
