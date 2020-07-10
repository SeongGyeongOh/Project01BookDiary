package com.osg.project01bookdiary_settings;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.osg.project01bookdiary.G;
import com.osg.project01bookdiary.LoginActivity;
import com.osg.project01bookdiary.MainActivity;
import com.osg.project01bookdiary.R;

public class Fragment05Settings extends Fragment {

    ImageView profileImg;
    TextView profileName, accountMan;
    Button btnChangeProf, btnLogout;

    RelativeLayout settingsLayout;

    ImageView iv;

    TextView linktoGP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_settings, container, false);

        profileImg = view.findViewById(R.id.iv_profile);
        profileName = view.findViewById(R.id.tv_nickname);
        settingsLayout = view.findViewById(R.id.settings_layout);

        Glide.with(getActivity()).load(G.profileUrl).into(profileImg);
        profileName.setText(G.profileName);

        //프로필 변경 버튼
        btnChangeProf = view.findViewById(R.id.btn_changeProfile);
        btnChangeProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                LayoutInflater inflater = getLayoutInflater();
                View v = getLayoutInflater().inflate(R.layout.alertdialog_settings, null );

                iv = v.findViewById(R.id.iv_clicktochangeimg);
                EditText et = v.findViewById(R.id.et_nickname);
                Button btn = v.findViewById(R.id.btn_changeProfile);

                setProfileImg();

                builder.setView(v);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        //로그아웃 버튼
        btnLogout= view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();

                        G.profileName = null;
                    }
                });

//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//
//                getActivity().finish();
            }
        });

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }

        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10&&grantResults[0]==PackageManager.PERMISSION_DENIED){
            Toast.makeText(getContext(), "외부 저장소 접근이 제한됩니다", Toast.LENGTH_SHORT).show();
        }
    }

    void setProfileImg(){
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
    }
}
