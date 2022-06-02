package com.example.graduationprojectclient.interfaces;

import com.example.graduationprojectclient.entity.Suggestion;

public interface ItemTouchHelperAdapter {

    void onItemDismiss(int position);
    void onItemReturned(int position);
    Suggestion findSuggestionByPosition(int position);
}
