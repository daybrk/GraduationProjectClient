package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.rv.SuggestionRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserSuggestions extends Fragment {

    List<Suggestion> suggestions;
    Button button;
    Button refresh;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_suggestions, container, false);

        button = view.findViewById(R.id.button_exit);
        refresh = view.findViewById(R.id.button_refresh);

        button.setOnClickListener(v -> {
            logOut();
        });

        refresh.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
            fragmentTransaction.commit();
        });

        getSuggestionByEmail(view);

        FloatingActionButton fab = view.findViewById(R.id.add_new_suggestion);
        fab.setOnClickListener(view1 -> {
            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new CreateSuggestion(), null);
            fragmentTransaction.commit();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    private void getSuggestionByEmail(View view) {
        Call<List<Suggestion>> call = CommunicationWithServerService.getApiService()
                .getSuggestionByEmail(LogInActivity.getInstance().getDb().loginDao().getLogin().getEmail());
        call.enqueue(new Callback<List<Suggestion>>() {
            @Override
            public void onResponse(@NonNull Call<List<Suggestion>> call, @NonNull Response<List<Suggestion>> response) {
                if (response.isSuccessful()) {
                    suggestions = response.body();
                    System.out.println(suggestions);
                    recyclerView = view.findViewById(R.id.suggestion_recycler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recyclerView.setAdapter(new SuggestionRecyclerView(suggestions));
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Suggestion>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void logOut() {
        Call<ResponseBody> call = CommunicationWithServerService.getApiService()
                .logout(LogInActivity.getInstance().getDb().loginDao().getLogin().getEmail());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                LogInActivity.getInstance().getDb().loginDao()
                        .delete(LogInActivity.getInstance().getDb().loginDao().getLogin());
                MainActivity.getInstance().logout();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }
}