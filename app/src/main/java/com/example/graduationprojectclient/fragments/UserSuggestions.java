package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.config.ConfigureRetrofit;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.entity.User;
import com.example.graduationprojectclient.rv.SuggestionRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserSuggestions extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<Suggestion> suggestions = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_user_suggestions, container, false);

        Call<List<Suggestion>> call = ConfigureRetrofit.getApiService().getSuggestionByEmail("h0st");
        call.enqueue(new Callback<List<Suggestion>>() {
            @Override
            public void onResponse(Call<List<Suggestion>> call, Response<List<Suggestion>> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject userJson = new JSONObject(String.valueOf(response));
                        for (int i = 0; i < response.body().size(); i++) {
                            System.out.println(response.body().get(i).getSuggestionDate());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Suggestion>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.add_new_suggestion);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.suggestion_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //TODO: Передать нормальные предложения, а не пустой список
        recyclerView.setAdapter(new SuggestionRecyclerView(suggestions));

        return view;
    }
}