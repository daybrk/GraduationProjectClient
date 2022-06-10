package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.rv.SimpleItemTouchHelperCallback;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.rv.SuggestionRecyclerView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagingSuggestion extends Fragment {

    List<Suggestion> suggestions;
    Button logOut;
    Button refresh;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_managing_suggestion, container, false);

        logOut = view.findViewById(R.id.button_exit);
        refresh = view.findViewById(R.id.button_refresh);
        recyclerView = view.findViewById(R.id.recycler);

        logOut.setOnClickListener(v -> {
           logOut();
        });

        refresh.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ManagingSuggestion(), null);
            fragmentTransaction.commit();
        });

        getUncheckedSuggestion(view);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
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

    private void getUncheckedSuggestion(View view) {
        Call<List<Suggestion>> call = CommunicationWithServerService.getApiService()
                .getUncheckedSuggestions(LogInActivity.getInstance().getDb().loginDao().getLogin().getEmail());
        call.enqueue(new Callback<List<Suggestion>>() {
            @Override
            public void onResponse(@NonNull Call<List<Suggestion>> call, @NonNull Response<List<Suggestion>> response) {
                if (response.isSuccessful()) {
                    suggestions = response.body();
                    SuggestionRecyclerView adapter = new SuggestionRecyclerView(suggestions);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recyclerView.setAdapter(adapter);

                    SimpleItemTouchHelperCallback callback =
                            new SimpleItemTouchHelperCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, adapter, view);
                    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                    touchHelper.attachToRecyclerView(recyclerView);
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
}