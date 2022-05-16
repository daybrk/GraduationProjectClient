package com.example.graduationprojectclient;

import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.entity.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiService {

    @GET("/login/{email}/{password}")
    @Headers("Content-type: application/json")
    Call<String> logIn(@Path("email") String email, @Path("password") String password);

    @GET("/user/{email}")
    @Headers("Content-type: application/json")
    Call<User> getUser(@Path("email") String email);

    @POST("/registration")
    Call<ResponseBody> createUser(@Body User user);

    @POST("/suggestion/create")
    Call<ResponseBody> createSuggestion(@Body Suggestion suggestion);

    @GET("/suggestion/{emailAuthor}")
    @Headers("Content-type: application/json")
    Call<List<Suggestion>> getSuggestionByEmail(@Path("emailAuthor") String email);

    @GET("/suggestion")
    @Headers("Content-type: application/json")
    Call<List<Suggestion>> getAllSuggestion();
}
