package com.example.graduationprojectclient;

import com.example.graduationprojectclient.entity.User;
import com.example.graduationprojectclient.entity.UserLogIn;

import java.util.List;

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

    @GET("/login/{email}/{password}")
    @Headers("Content-type: application/json")
    Call<String> logIn(@Path("email") String email, @Path("password") String password);

    @GET("/user")
    Call<List<User>> getUsers();

    @POST("/registration")
    Call<ResponseBody> createUser(@Body User user);
}
