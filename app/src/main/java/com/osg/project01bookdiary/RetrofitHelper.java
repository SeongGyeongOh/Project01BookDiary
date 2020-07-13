package com.osg.project01bookdiary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {
    public static Retrofit getString(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("http://kamniang.dothome.co.kr");
        builder.addConverterFactory(ScalarsConverterFactory.create());

        return builder.build();
    }

    public static Retrofit getJsonFromDB(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("http://kamniang.dothome.co.kr");
        builder.addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }

    //카카오 로그인 API용 Retrofit
    public static Retrofit getJson(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://dapi.kakao.com");
        builder.addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }

}
