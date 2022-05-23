package com.example.graduationprojectclient.interfaces;

import com.example.graduationprojectclient.entity.Suggestion;

import java.util.List;

public interface ItemTouchHelperAdapter {

    void onItemDismiss(int position);
    void onItemReturned(int position);
    Suggestion findSuggestionByPosition(int position);
}
