package com.osg.project01bookdiary;

import android.app.SearchManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Fragment02SearchBook extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_searchbook, container, false);

        androidx.appcompat.widget.SearchView searchView = view.findViewById(R.id.searchview);
//        ImageView iv = view.findViewById(R.id.iv);
//        TextView title = view.findViewById(R.id.tv_title);
//        TextView author = view.findViewById(R.id.tv_author);
        ImageView closeButton = view.findViewById(R.id.search_close_btn);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        ArrayList<Document> items = new ArrayList<>();
        RecyclerSearchBookAdapter adapter = new RecyclerSearchBookAdapter(items, getContext());

        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getContext(), query+"", Toast.LENGTH_SHORT).show();
                Retrofit retrofit = RetrofitHelper.getJson();
                RetrofitService service = retrofit.create(RetrofitService.class);
                Call<VOBookItem> call = service.getJson(query);

                call.enqueue(new Callback<VOBookItem>() {
                    @Override
                    public void onResponse(Call<VOBookItem> call, Response<VOBookItem> response) {
                        if(response.isSuccessful()){
                            VOBookItem VO = response.body();
                            ArrayList<Document> documents = VO.documents;
                            for (Document document : documents){
                                items.add(document);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<VOBookItem> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText et = view.findViewById(R.id.search_src_text);
//                et.setText("");
                searchView.setQuery("", true);
                items.clear();
                adapter.notifyDataSetChanged();
            }
        });












        return view;
    }
}
