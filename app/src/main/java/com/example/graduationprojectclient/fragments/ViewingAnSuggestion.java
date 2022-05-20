package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.Suggestion;


public class ViewingAnSuggestion extends Fragment {

    Suggestion suggestionData;

    public ViewingAnSuggestion(Suggestion suggestion) {
        this.suggestionData = suggestion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viewing_an_suggestion, container, false);

        TextView suggestionTheme = view.findViewById(R.id.tv_suggestion_theme);
        TextView suggestion = view.findViewById(R.id.tv_suggestion);
        TextView suggestionDate = view.findViewById(R.id.tv_suggestion_date);

        suggestionTheme.setText(suggestionData.getSuggestionTheme());
        suggestion.setText(suggestionData.getSuggestion());
        suggestionDate.setText(suggestionData.getSuggestionDate());

        return view;
    }
}