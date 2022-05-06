package com.example.graduationprojectclient.entity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Suggestion {

    String suggestionTheme;
    String suggestion;
    String suggestionDate;
    String suggestionStatus;
    String suggestionAuthor;

    public Suggestion(String suggestionTheme, String suggestion, String suggestionDate, String suggestionStatus, String suggestionAuthor) {
        this.suggestionTheme = suggestionTheme;
        this.suggestion = suggestion;
        this.suggestionDate = suggestionDate;
        this.suggestionStatus = suggestionStatus;
        this.suggestionAuthor = suggestionAuthor;
    }

    public String getSuggestionTheme() {
        return suggestionTheme;
    }

    public void setSuggestionTheme(String suggestionTheme) {
        this.suggestionTheme = suggestionTheme;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSuggestionDate() {
        return suggestionDate;
    }

    public void setSuggestionDate(String suggestionDate) {
        this.suggestionDate = suggestionDate;
    }

    public String getSuggestionStatus() {
        return suggestionStatus;
    }

    public void setSuggestionStatus(String suggestionStatus) {
        this.suggestionStatus = suggestionStatus;
    }

    public String getSuggestionAuthor() {
        return suggestionAuthor;
    }

    public void setSuggestionAuthor(String suggestionAuthor) {
        this.suggestionAuthor = suggestionAuthor;
    }
}
