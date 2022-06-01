package com.example.graduationprojectclient.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.graduationprojectclient.interfaces.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommunicationWithServerService extends Service {


    private static ApiService apiService;
    private static Retrofit retrofit;
        public static final String BASE_URL = "http://192.168.0.101:8081";
//    public static final String BASE_URL = "http://192.168.20.162:8081";
    private static String AUTH_KEY = "";

    public CommunicationWithServerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public static ApiService getApiService() {
        return apiService;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static String getAuthKey() {
        return AUTH_KEY;
    }

    public static void setAuthKey(String authKey) {
        AUTH_KEY = authKey;
    }


}