package com.osg.project01bookdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerSearchBookAdapter extends RecyclerView.Adapter {

    String imgUrl;

    ArrayList<Document> items;
    Context context;

    public RecyclerSearchBookAdapter(ArrayList<Document> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_searchbook, parent, false);
        VH viewHolder = new VH(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        Document item = items.get(position);

        if(item.thumbnail.isEmpty()){
            vh.iv.setImageResource(R.drawable.noimage);
        }else {
            Glide.with(context).load(item.thumbnail).into(vh.iv);
        }

//        Log.i("thum", imgUrl);

        vh.tvTitle.setText(item.title);
        ArrayList<String> authors = item.authors;
        for (String author : authors) {
            vh.tvAuthor.append(author + "  ");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvTitle;
        TextView tvAuthor;
        ImageButton imgBtn;

        public VH(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.ivTitle);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            imgBtn = itemView.findViewById(R.id.img_btn);

            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, imgBtn, 0);
                    popupMenu.inflate(R.menu.menu_searchbookcontext);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getTitle().toString()){
                                case "기록 남기기":
                                    Intent intent = new Intent(context, AddRevActivity.class);
                                    intent.putExtra("image", items.get(getLayoutPosition()).thumbnail);
//                                    Log.i("iv", imgUrl);
                                    intent.putExtra("title", items.get(getLayoutPosition()).title);
                                    intent.putExtra("author", tvAuthor.getText().toString());
                                    context.startActivity(intent);
                                    ((AppCompatActivity)context).finish();
                                    break;

                                case "내 도서 목록 추가":
                                    //다이아로그를 띄우 책 선택 여부/별점 매기기
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });

        }
    }
}
