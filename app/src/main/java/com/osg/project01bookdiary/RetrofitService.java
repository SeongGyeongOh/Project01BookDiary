package com.osg.project01bookdiary;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.osg.project01bookdiary_Calendar.PushData;
import com.osg.project01bookdiary_sharedreview.SharedReview_item;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitService {
    @FormUrlEncoded
    @POST("/Project01BookDiary/uploadData.php")
    Call<String> getLoginData(@Field("id") String nickName);

    @Headers("Authorization: KakaoAK 3893216dd99e3e1a539699708c60d665")
    @GET("/v3/search/book.json?target=title&size=50")
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

    //내 리뷰만 모아보기
    @FormUrlEncoded
    @POST("/Project01BookDiary/selectMyReview.php")
    Call<ArrayList<SharedReview_item>> showMyReview(@Field("ID") String ID);

    //삭제 버튼 눌러 table+Nickname의 정보 삭제
    @FormUrlEncoded
    @POST("/Project01BookDiary/deleteDataFromDB.php")
    Call<String> deleteDataFromDB(@Field("tableName") String tableName, @Field("no") int no);

    //설정에서 프로필 이미지 변경 시 해당 내용 반영
    @FormUrlEncoded
    @POST("/Project01BookDiary/updateProfileImageTest.php")
    Call<String> updateProfileImage(@Field("ID") String ID,
                                    @Field("profileImage") String profileImage);

    //설정에서 프로필 이름 변경 시 해당 내용 반영
    @FormUrlEncoded
    @POST("/Project01BookDiary/uptdateProfileNameToData.php")
    Call<String> updateProfileName(@Field("ID") String ID,
                                   @Field("profileName") String profileName);

    //푸시로 받을 데이터 설정
    @FormUrlEncoded
    @POST("/Project01BookDiary/pushData.php")
    Call<String> uploadPushData(@Field("title") String title, @Field("message") String msg, @Field("token") String token);

    //공유한 리뷰 삭제
    @FormUrlEncoded
    @POST("/Project01BookDiary/deleteMySharedReview.php")
    Call<String> deleteMySharedReview(@Field("no") int no);


}
