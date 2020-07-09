package com.osg.project01bookdiary;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @FormUrlEncoded
    @POST("/Project01BookDiary/uploadData.php")
    Call<String> getLoginData(@Field("nickName") String nickName);

    @Headers("Authorization: KakaoAK 3893216dd99e3e1a539699708c60d665")
    @GET("/v3/search/book.json?target=title")
    Call<VOBookItem> getJson(@Query("query") String path);
}
