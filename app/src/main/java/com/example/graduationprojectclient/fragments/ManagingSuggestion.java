package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.rv.SimpleItemTouchHelperCallback;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.rv.SuggestionRecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagingSuggestion extends Fragment {

    List<Suggestion> suggestions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_managing_suggestion, container, false);

        Call<List<Suggestion>> call = CommunicationWithServerService.getApiService().getAllSuggestion();
        call.enqueue(new Callback<List<Suggestion>>() {
            @Override
            public void onResponse(Call<List<Suggestion>> call, Response<List<Suggestion>> response) {
                if (response.isSuccessful()) {

                    suggestions = response.body();

                    SuggestionRecyclerView adapter = new SuggestionRecyclerView(suggestions);
                    RecyclerView recyclerView = view.findViewById(R.id.recycler);
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
            public void onFailure(Call<List<Suggestion>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //TODO: Подтверждение выхода, или что-нибудь другое
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }
}