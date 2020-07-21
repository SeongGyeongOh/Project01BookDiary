package com.osg.project01bookdiary_settings;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.auth.authorization.AuthorizationResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.osg.project01bookdiary.G;
import com.osg.project01bookdiary.LoginActivity;
import com.osg.project01bookdiary.MainActivity;
import com.osg.project01bookdiary.R;
import com.osg.project01bookdiary.RetrofitHelper;
import com.osg.project01bookdiary.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Url;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Fragment05Settings extends Fragment {

    CircleImageView profileImg;
    TextView profileName, accountMan;
    Button btnChangeProf, btnLogout;

    RelativeLayout settingsLayout;

    ImageView iv;
    Uri img;
    TextView linktoGP;

    Uri imgUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_settings, container, false);

        //퍼미션 추가
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }

        profileImg = view.findViewById(R.id.iv_profile);
        profileName = view.findViewById(R.id.tv_nickname);
        settingsLayout = view.findViewById(R.id.settings_layout);

        //TODO : 한 번만 로그인을 했어도!! 시간이 지나도!! 제발!! 데이터 안날아가게 설정할 것

        Glide.with(getActivity()).load(G.profileUrl).into(profileImg);
        profileName.setText(G.profileName);

        //프로필 변경 버튼
        btnChangeProf = view.findViewById(R.id.btn_changeProfile);
        btnChangeProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v = getLayoutInflater().inflate(R.layout.alertdialog_settings, null );
                iv = v.findViewById(R.id.iv_clicktochangeimg);
                EditText et = v.findViewById(R.id.et_nickname);
                setProfileImg();

                builder.setView(v);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(img!=null){
                            G.profileUrl=img.toString();
                            Glide.with(getContext()).load(img).into(profileImg);
                            //imgIsChanged = true;

                            //1. 사진을 Firebase Storage에 전송
                            imgUrl = Uri.parse(G.profileUrl);

                            saveProfileData();

                            //프로필 이미지 SharedPreferences에 저장하기
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Profile", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Profile Image", G.profileUrl);

                            editor.commit();
                        }

                        if(et.getText()!=null){
                            G.profileName=et.getText().toString();
                            profileName.setText(G.profileName);

                            Retrofit retrofit = RetrofitHelper.getString();
                            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                            Call<String> call = retrofitService.updateProfileName(G.nickName, G.profileName);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(getContext(), response.body()+"", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });


                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Profile", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Profile Name", G.profileName);

                            editor.commit();

                        }else{
                            return;
                        }
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
                G.profileName = null;
                Log.i("LOGOUT", "로그아웃 성공");
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                    }
                });
                redirectLoginActivity();
            }
        });

        return view;
    }


    void redirectLoginActivity(){
        //로그아웃시 로그인 액티비티로 이동
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10&&grantResults[0]==PackageManager.PERMISSION_DENIED){
            Toast.makeText(getContext(), "외부 저장소 접근이 제한됩니다", Toast.LENGTH_SHORT).show();
        }
    }

    //사진 폴더로 가는 인텐트 실행
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

    //사진 가지고 오는 Activity의 결과
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            img = data.getData();
            if(img!=null){
                Glide.with(getContext()).load(img).into(iv);
            }
        }
    }

    public void saveProfileData(){
        //프로필 이미지 Firebase에 저장하기
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        String profileImgName ="Image"+G.nickName+".png";

        StorageReference ref = firebaseStorage.getReference("profileImage/"+profileImgName);

        UploadTask task = ref.putFile(imgUrl);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "업로드 성공", Toast.LENGTH_SHORT).show();

                StorageReference imgRef = firebaseStorage.getReference();
                imgRef.child("profileImage/"+"Image"+G.nickName+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        G.profileUrl = uri.toString();
                        String uriString = G.profileUrl;
                        Toast.makeText(getContext(), G.profileUrl+"", Toast.LENGTH_SHORT).show();

                        //2. 전송한 사진의 Firebase 주소값을 얻어와 DB에 반영
                        Retrofit retrofit = RetrofitHelper.getString();
                        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                        Call<String> call = retrofitService.updateProfileImage(G.nickName, uriString);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(getContext(), response.body()+"", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage()+"", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    void loadProfileData(){
        SharedPreferences sharedPreferences =getContext().getSharedPreferences("Profile", MODE_PRIVATE);
        G.profileUrl = sharedPreferences.getString("Profile Image", null);
        G.profileName = sharedPreferences.getString("Profile Name", null);
    }

}
