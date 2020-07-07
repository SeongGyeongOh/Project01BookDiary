package com.osg.project01bookdiary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {
    public static Retrofit getString(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://dapi.kakao.com");
        builder.addConverterFactory(ScalarsConverterFactory.create());

        return builder.build();
    }

    public static Retrofit getJson(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://dapi.kakao.com");
        builder.addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }
}