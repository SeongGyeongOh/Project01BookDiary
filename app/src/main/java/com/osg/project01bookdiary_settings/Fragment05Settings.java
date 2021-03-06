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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.auth.authorization.AuthorizationResult;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.osg.project01bookdiary.G;
import com.osg.project01bookdiary.LoginActivity;
import com.osg.project01bookdiary.MainActivity;
import com.osg.project01bookdiary.R;
import com.osg.project01bookdiary.RetrofitHelper;
import com.osg.project01bookdiary.RetrofitService;
import com.osg.project01bookdiary_sharedreview.RecyclerCommentItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Button btnChangeProf, btnLogout, btnLinktoGP, btnOut;
    RelativeLayout settingsLayout;
    ImageView iv;
    Uri img;
//    String imageUrl, nickName;
    String profileUrl, profileNickname;

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

        loadProfileData();
        //프로필 이미지와 이름
        if(G.settingProfileImg==null&&G.loginProfileImg==null) {
            Glide.with(getActivity()).load(R.drawable.kakao_default_profile_image).into(profileImg);
        }else if(G.settingProfileImg==null){
            Glide.with(getActivity()).load(G.loginProfileImg).into(profileImg);
        }else {
            Glide.with(getActivity()).load(G.settingProfileImg).into(profileImg);
        }

        if(G.settingProfileName==null){
            profileName.setText(G.loginProfileName);
        }else {
            profileName.setText(G.settingProfileName);
        }

        //프로필 변경 버튼
        btnChangeProf = view.findViewById(R.id.btn_changeProfile);
        btnChangeProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v = getLayoutInflater().inflate(R.layout.alertdialog_settings, null );
                iv = v.findViewById(R.id.iv_clicktochangeimg);
                EditText et = v.findViewById(R.id.et_nickname);

                //인텐트로 이미지 가져오기
                setProfileImg();
                builder.setView(v);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(img!=null){
                            Glide.with(getContext()).load(img.toString()).into(profileImg);
                            //1. 사진을 SharedPreferencs에 저장
                            //2. 프로필 이미지 SharedPreferences에 저장하기
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Profile"+G.nickName, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Setting Profile Image", img.toString());
                            editor.commit();

                            //1. 사진을 Firebase Storage에 전송
                            Uri imgUrl = Uri.parse(img.toString());
                            saveProfileData(imgUrl);
                        }

                        if(et.getText().toString().isEmpty()){
                            return;
                        }else {
                            //이름정보 저장
                            profileNickname=et.getText().toString();
                            profileName.setText(profileNickname);

                            //DB에 반영
                            Retrofit retrofit = RetrofitHelper.getString();
                            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                            Call<String> call = retrofitService.updateProfileName(G.nickName, profileNickname);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {}
                            });

                            //코멘트에 변경된 프로필 이름 반영
                            FirebaseDatabase.getInstance().getReference().child("comment").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    for(DataSnapshot shots : snapshot.getChildren()){
                                        if(shots.getKey().equals(G.nickName)){
                                            for(DataSnapshot shot : shots.getChildren()){
                                                shot.getRef().child("nickName").setValue(et.getText().toString());
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {                                }
                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {                                }
                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {                                }
                            });

                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Profile"+G.nickName, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Setting Profile Name", et.getText().toString());
                            editor.commit();
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
                G.nickName = null;
                Log.i("LOGOUT", "로그아웃 성공");
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                    }
                });
                redirectLoginActivity();
            }
        });

        //구글 플레이로 이동하는 버튼
        btnLinktoGP=view.findViewById(R.id.btn_linktoGP);
        btnLinktoGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.osg.project01bookdiary"));
                startActivity(intent);
            }
        });

        //회원탈퇴 버튼
       btnOut=view.findViewById(R.id.btn_out);
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("탈퇴하면 저장한 모든 정보가 사라집니다.\n탈퇴 하시겠습니까?")
                        .setPositiveButton("확인", (dialogInterface, i) -> {
                            //탈퇴 코드
                            UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                @Override
                                public void onSessionClosed(ErrorResult errorResult) { }
                                @Override
                                public void onSuccess(Long result) {
                                    //MySql DB의 모든 정보 지우기
                                    Retrofit retrofit=RetrofitHelper.getString();
                                    RetrofitService service=retrofit.create(RetrofitService.class);
                                    Call<String> call=service.deleteUserData(G.nickName);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {

                                        }
                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {}
                                    });

                                    //Firebase database 의 정보 지우기
                                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                                    DatabaseReference ref=db.getReference().child("Calendar"+G.nickName);
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.getRef().removeValue();
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {}
                                    });

                                    db.getReference().child("comment")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot shots : snapshot.getChildren()){
                                                        shots.getRef().child(G.nickName).removeValue();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {}
                                            });

                                    //FirebaseStorage의 정보 지우기
                                    FirebaseStorage storage=FirebaseStorage.getInstance();
                                    StorageReference refStore=storage.getReference().child("profileImage/"+"Image"+G.nickName+".png");
                                    refStore.delete();

                                    //Shared 정보 지우기
                                    SharedPreferences pref=getActivity().getSharedPreferences("Profile"+G.nickName, MODE_PRIVATE);
                                    pref.edit().clear().commit();

                                    Toast.makeText(getContext(), "탈퇴했습니다", Toast.LENGTH_SHORT).show();

                                    redirectLoginActivity();
                                }
                            });
                        }).setNegativeButton("취소", (dialogInterface, i) -> {}).show();
            }
        });


        return view;
    }//onCreateView

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10&&grantResults[0]==PackageManager.PERMISSION_DENIED){
            Toast.makeText(getContext(), "외부 저장소 접근이 제한됩니다", Toast.LENGTH_SHORT).show();
        }
    }

    //로그아웃시 로그인 액티비티로 이동
    void redirectLoginActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
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

    //프로필 이미지 저장하기
    public void saveProfileData(Uri imgUrl){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        String profileImgName ="Image"+G.nickName+".png";
        StorageReference ref = firebaseStorage.getReference("profileImage/"+profileImgName);
        UploadTask task = ref.putFile(imgUrl);
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference imgRef = firebaseStorage.getReference();
                imgRef.child("profileImage/"+"Image"+G.nickName+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileUrl = uri.toString();
                        //1. 전송한 사진의 Firebase 주소값을 얻어와 DB에 반영
                        Retrofit retrofit = RetrofitHelper.getString();
                        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                        Call<String> call = retrofitService.updateProfileImage(G.nickName, profileUrl);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {}
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

    //Shared에 저장한 정보 읽어오기
    void loadProfileData(){
        SharedPreferences sharedPreferences =getContext().getSharedPreferences("Profile"+G.nickName, MODE_PRIVATE);
        G.settingProfileImg=sharedPreferences.getString("Setting Profile Image", null);
        G.settingProfileName=sharedPreferences.getString("Setting Profile Name", null);
    }

}
