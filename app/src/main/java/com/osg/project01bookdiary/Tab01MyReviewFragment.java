package com.osg.project01bookdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Tab01MyReviewFragment extends Fragment {

    ArrayList<Tab01myreview_item> items = new ArrayList<>();
    RecyclerAdapter myAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab01_layout, container, false);

        refreshLayout=view.findViewById(R.id.swipe);

        myAdapter = new RecyclerAdapter(items, getContext());
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(myAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Retrofit retrofit = RetrofitHelper.getJsonFromDB();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ArrayList<Tab01myreview_item>> call = retrofitService.getReviewDataJson(G.nickName);

        call.enqueue(new Callback<ArrayList<Tab01myreview_item>>() {
            @Override
            public void onResponse(Call<ArrayList<Tab01myreview_item>> call, Response<ArrayList<Tab01myreview_item>> response) {
                if(response.isSuccessful()){
                    ArrayList<Tab01myreview_item> reviews = response.body();

                    items.clear();
                    myAdapter.notifyDataSetChanged();
//                    Toast.makeText(getActivity(), "MainActivity 시작", Toast.LENGTH_SHORT).show();

                    for(Tab01myreview_item item: reviews){
                        items.add(0, item);

                        myAdapter.notifyItemInserted(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tab01myreview_item>> call, Throwable t) {

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }

}

