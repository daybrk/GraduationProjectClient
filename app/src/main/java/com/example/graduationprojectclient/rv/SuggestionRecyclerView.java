package com.example.graduationprojectclient.rv;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationprojectclient.ItemTouchHelperAdapter;
import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.fragments.CreateSuggestion;
import com.example.graduationprojectclient.fragments.ViewingAnSuggestion;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SuggestionRecyclerView extends RecyclerView.Adapter<SuggestionRecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Suggestion> suggestions;
    Map<Integer, Suggestion> deletedSuggestion ;

    public SuggestionRecyclerView(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
        deletedSuggestion = new HashMap<>();
    }

    @NonNull
    @Override
    public SuggestionRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionRecyclerView.ViewHolder holder, int position) {
        holder.suggestionTheme.setText(suggestions.get(position).getSuggestionTheme());
        holder.suggestionShorText.setText(suggestions.get(position).getSuggestion());
        holder.suggestionDate.setText(suggestions.get(position).getSuggestionDate());
        holder.suggestionStatus.setText(suggestions.get(position).getSuggestionStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ViewingAnSuggestion(suggestions.get(position)), null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    @Override
    public void onItemDismiss(int position) {
        deletedSuggestion.put(position, suggestions.get(position));
        suggestions.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemReturned(int position) {
        suggestions.add(position, deletedSuggestion.get(position));
        notifyItemInserted(position);
    }

    @Override
    public Suggestion findSuggestionByPosition(int position) {
        return deletedSuggestion.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView suggestionTheme, suggestionShorText, suggestionDate, suggestionStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            suggestionTheme = itemView.findViewById(R.id.suggestion_theme);
            suggestionShorText = itemView.findViewById(R.id.suggestion_short_text);
            suggestionDate = itemView.findViewById(R.id.suggetion_date);
            suggestionStatus = itemView.findViewById(R.id.suggetion_status);
        }
    }
}
