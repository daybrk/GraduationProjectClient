package com.example.graduationprojectclient;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.graduationprojectclient.config.ConfigureRetrofit;
import com.example.graduationprojectclient.entity.AuthResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommunicationWithServerService extends Service {

    private static String EMAIL;
    private static String ROLE;
    private static boolean isRunning = false;

    private static ApiService apiService;
    private static Retrofit retrofit;
    //    public static final String BASE_URL = "http://192.168.0.102:8081";
    public static final String BASE_URL = "http://192.168.20.162:8081";
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    "MyChannelId",
                    "My Foreground Service",
                    NotificationManager.IMPORTANCE_NONE);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(channel);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    this, "MyChannelId");

            Notification notification = notificationBuilder
                    .setChannelId("MyChannelId")
                    .build();

            startForeground(1, notification);
        }

        if (!isRunning) {
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
            isRunning = true;
        }

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

    public static String getEMAIL() {
        return EMAIL;
    }

    public static void setEMAIL(String EMAIL) {
        CommunicationWithServerService.EMAIL = EMAIL;
    }

    public static String getROLE() {
        return ROLE;
    }

    public static void setROLE(String ROLE) {
        CommunicationWithServerService.ROLE = ROLE;
    }

    public static boolean isRunning() {
        return isRunning;
    }
}