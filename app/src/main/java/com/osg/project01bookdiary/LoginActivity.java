package com.osg.project01bookdiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.kakao.util.helper.Utility.getPackageInfo;


public class LoginActivity extends AppCompatActivity {

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        String keyHash = getKeyHash(this);
//        Log.i("TAG", keyHash);
        if(Session.getCurrentSession().checkAndImplicitOpen()){
            //Shared에 있는 값이 널이 아니면!
            if (G.nickName!=null) {
                Intent intent = new Intent(this, MainActivity.class );
                startActivity(intent);
                finish();
            }else {
                requestUserInfo();
            }
        }else {
            Session.getCurrentSession().addCallback(iSessionCallback);

        }
    }

    private ISessionCallback iSessionCallback = new ISessionCallback() {
        @Override
        public void onSessionOpened() {
            Log.i("KAKAO", "로그인 성공");
            requestUserInfo();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("KAKAO", "로그인 실패", exception);
        }
    };

    void requestUserInfo(){
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {         }
            @Override
            public void onSuccess(MeV2Response result) {
                long id = result.getId();
//                Log.i("ID", id+"");
                G.nickName = "table"+id;

                UserAccount account = result.getKakaoAccount();
                if(account==null) return;
                profile = account.getProfile();
                G.profileName = profile.getNickname();
                G.profileUrl = profile.getProfileImageUrl();

                setProfile();

                //카카로 로그인 id(G.nickName)을 이용해서 DB에 테이블 생성
                Retrofit retrofit = RetrofitHelper.getString();
                RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                Call<String> call = retrofitService.getLoginData(G.nickName);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {}
                });
            }
        });
    }


    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("TAG", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(iSessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveProfile(String profileName, String profileUrl){
        //SharedPreference에 닉네임/프로필 이미지 Url 저장
        SharedPreferences sharedPreferences=getSharedPreferences("Profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Profile Name", profileName);
        editor.putString("Profile Image", profileUrl);
        editor.commit();
    }

    private void loadProfile(){
        SharedPreferences pref=getSharedPreferences("Profile", MODE_PRIVATE);
        pref.getString("Profile Name", "");
        pref.getString("Profile Image", "");
    }

    private void setProfile(){
        FirebaseStorage storage=FirebaseStorage.getInstance();
        String profileImgName ="Image"+G.nickName+".png";

        SharedPreferences sharedPreferences =getSharedPreferences("Profile"+G.nickName, MODE_PRIVATE);
        String imageUrl=sharedPreferences.getString("Profile Image", null);
        String nickName=sharedPreferences.getString("Profile Name", null);
        if(imageUrl==null){
//            StorageReference reference=storage.getReference("profileImage/"+profileImgName);
//            UploadTask task=reference.putFile(Uri.parse(G.profileUrl));
//            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(MainActivity.this, "성공", Toast.LENGTH_SHORT).show();
//                }
//            });

            Retrofit retrofit1=RetrofitHelper.getString();
            RetrofitService service=retrofit1.create(RetrofitService.class);
            Call<String> call1=service.updateProfileImage(G.nickName, G.profileUrl);
            call1.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(LoginActivity.this, response.body()+"", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
        if(nickName==null){
            Retrofit retrofit=RetrofitHelper.getString();
            RetrofitService retrofitService=retrofit.create(RetrofitService.class);
            Call<String> call=retrofitService.updateProfileName(G.nickName, G.profileName);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(LoginActivity.this, response.body()+"", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }
}