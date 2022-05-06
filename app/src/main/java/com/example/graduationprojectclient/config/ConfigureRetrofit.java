package com.example.graduationprojectclient.config;

import com.example.graduationprojectclient.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigureRetrofit {

    private static ApiService apiService;
    private static Retrofit retrofit;
//    public static final String BASE_URL = "http://192.168.0.102:8081";
    public static final String BASE_URL = "http://192.168.20.162:8081";

    public static void createRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient);

        retrofit = retrofitBuilder.build();

        apiService = retrofit.create(ApiService.class);

    }

    public static ApiService getApiService() {
        return apiService;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
