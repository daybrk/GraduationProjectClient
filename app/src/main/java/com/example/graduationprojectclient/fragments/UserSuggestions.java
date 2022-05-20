package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationprojectclient.CommunicationWithServerService;
import com.example.graduationprojectclient.MainActivity;
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

    List<Suggestion> suggestions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_suggestions, container, false);

        Call<List<Suggestion>> call = CommunicationWithServerService.getApiService().getSuggestionByEmail(CommunicationWithServerService.getEMAIL());
        call.enqueue(new Callback<List<Suggestion>>() {
            @Override
            public void onResponse(Call<List<Suggestion>> call, Response<List<Suggestion>> response) {
                if (response.isSuccessful()) {
                    suggestions = response.body();
                    RecyclerView recyclerView = view.findViewById(R.id.suggestion_recycler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recyclerView.setAdapter(new SuggestionRecyclerView(suggestions));
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
                FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new CreateSuggestion(), null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}