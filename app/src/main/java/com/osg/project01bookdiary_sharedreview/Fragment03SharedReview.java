package com.osg.project01bookdiary_sharedreview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.osg.project01bookdiary.R;
import com.osg.project01bookdiary.RetrofitHelper;
import com.osg.project01bookdiary.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Fragment03SharedReview extends Fragment {

    ArrayList<SharedReview_item> items = new ArrayList<>();
    RecyclerView recyclerView;
    SharedReviewAdapter sharedReviewAdapter;
    SwipeRefreshLayout swipelayout;

    Button btnMyReview;

    //https://github.com/Applandeo/Material-Calendar-View#dots-indicator (사용한 캘린더 깃헙 주소)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_sharedreview, container, false);

        swipelayout=view.findViewById(R.id.swipelay);
        btnMyReview=view.findViewById(R.id.btn_myReview);

        recyclerView = view.findViewById(R.id.sharedRecyclerView);
        sharedReviewAdapter = new SharedReviewAdapter(getContext(), items);
        recyclerView.setAdapter(sharedReviewAdapter);

        items.clear();
        sharedReviewAdapter.notifyDataSetChanged();
        showShraedReview();

        btnMyReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                showShraedReview();
                swipelayout.setRefreshing(false);
            }
        });

        return view;
    }



    //공유 데이터 DB에서 읽어와 보여주기
    public void showShraedReview(){
        Retrofit retrofit = RetrofitHelper.getJsonFromDB();
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);
        Call<ArrayList<SharedReview_item>> call = retrofitService.loadSharedData();
        call.enqueue(new Callback<ArrayList<SharedReview_item>>() {
            @Override
            public void onResponse(Call<ArrayList<SharedReview_item>> call, Response<ArrayList<SharedReview_item>> response) {
                ArrayList<SharedReview_item> lists = response.body();

                for(SharedReview_item item : lists){
                    items.add(0, item);
                    sharedReviewAdapter.notifyItemInserted(0);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SharedReview_item>> call, Throwable t) {

            }
        });
    }

}
