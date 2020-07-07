package com.osg.project01bookdiary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
//    @Headers("Authorization: KakaoAK 3893216dd99e3e1a539699708c60d665")
//    @GET("/v3/search/book.json?target=title&query=미움받을 용기")
//    Call<String> getString();

    @Headers("Authorization: KakaoAK 3893216dd99e3e1a539699708c60d665")
    @GET("/v3/search/book.json?target=title")
    Call<VOBookItem> getJson(@Query("query") String path);
}
