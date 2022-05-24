package com.example.graduationprojectclient.interfaces;

import com.example.graduationprojectclient.entity.AuthResponse;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.entity.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ApiService {

    @GET("/login/{email}/{password}")
    @Headers("Content-type: application/json")
    Call<AuthResponse> logIn(@Path("email") String email, @Path("password") String password);

    @POST("/registration")
    Call<ResponseBody> createUser(@Body User user);

    @POST("/suggestion/create")
    Call<ResponseBody> createSuggestion(@Body Suggestion suggestion);

    @PUT("/suggestion/confirm/{suggestionId}/{suggestionInspector}")
    Call<ResponseBody> confirmSuggestion(@Path("suggestionId") Long suggestionId, @Path("suggestionInspector") String suggestionInspector);

    @DELETE("/suggestion/delete/{suggestionId}/{suggestionInspector}")
    Call<ResponseBody> deleteSuggestion(@Path("suggestionId") Long suggestionId, @Path("suggestionInspector") String suggestionInspector);

    @GET("/suggestion/{emailAuthor}")
    @Headers("Content-type: application/json")
    Call<List<Suggestion>> getSuggestionByEmail(@Path("emailAuthor") String email);

    @GET("/unchecked/suggestion/{emailInspector}")
    @Headers("Content-type: application/json")
    Call<List<Suggestion>> getUncheckedSuggestions(@Path("emailInspector") String email);

    @GET("/suggestion")
    @Headers("Content-type: application/json")
    Call<List<Suggestion>> getAllSuggestion();
}
