package com.example.graduationprojectclient;

import com.example.graduationprojectclient.entity.User;
import com.example.graduationprojectclient.entity.UserLogIn;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface ApiService {

    @POST("/login")
    Call<ResponseBody> logIn(@Body UserLogIn user);

    @GET("/user/{email}")
    Call<Response<String>> getUser(@Path("email") String email);
}
