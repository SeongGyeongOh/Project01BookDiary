package com.osg.project01bookdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Tab02MyBookListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerMybookListAdapter adapter;
    ArrayList<Tab01myreview_item> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab02_layout, container, false);

        recyclerView = view.findViewById(R.id.recycler_grid);
        adapter = new RecyclerMybookListAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = RetrofitHelper.getJsonFromDB();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ArrayList<Tab01myreview_item>> call = retrofitService.getReviewDataJson(G.nickName);

        call.enqueue(new Callback<ArrayList<Tab01myreview_item>>() {
            @Override
            public void onResponse(Call<ArrayList<Tab01myreview_item>> call, Response<ArrayList<Tab01myreview_item>> response) {
                if(response.isSuccessful()){
                    ArrayList<Tab01myreview_item> reviews = response.body();
                    items.clear();
                    adapter.notifyDataSetChanged();
                    for(Tab01myreview_item item : reviews){
                        items.add(0, item);
                        adapter.notifyItemInserted(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tab01myreview_item>> call, Throwable t) {       }
        });

        return view;
    }
}
