package com.osg.project01bookdiary;

import com.osg.project01bookdiary_sharedreview.SharedReview_item;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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

    //공유 버튼 눌렀을 때 데이터를 SharedReview 데이터베이스 테이블로 전송
    @POST("/Project01BookDiary/updateSharedReview.php")
    Call<String> updateSharedReview(@Body SharedItem item);

    //Fragment03SharedReview가 DB에서 읽어옴
    @GET("/Project01BookDiary/loadSharedReview.php")
    Call<ArrayList<SharedReview_item>> loadSharedData();
}
