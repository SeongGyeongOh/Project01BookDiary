package com.osg.project01bookdiary;

import java.util.ArrayList;

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
    Call<String> getLoginData(@Field("id") String nickName);

    @Headers("Authorization: KakaoAK 3893216dd99e3e1a539699708c60d665")
    @GET("/v3/search/book.json?target=title")
    Call<VOBookItem> getJson(@Query("query") String path);

    @FormUrlEncoded
    @POST("/Project01BookDiary/insertDataToDB.php")
    Call<String> postReviewData(@Field("nickName") String nickName,
                                @Field("image") String image,
                                @Field("bookTitle") String title,
                                @Field("bookAuthor") String author,
                                @Field("reviewTitle") String revTitle,
                                @Field("reviewContent") String revContent);

    @GET("/Project01BookDiary/getDataFromDB.php")
    Call<ArrayList<Tab01myreview_item>> getReviewDataJson(@Query("nickName") String nickName);

    @FormUrlEncoded
    @POST("/Project01BookDiary/updateDataContent.php")
    Call<String> updateDBdate(@Field("nickName") String nickName,
                              @Field("reviewTitle") String title,
                              @Field("reviewContent") String content,
                              @Field("bookTitle") String bookTitle);

}
