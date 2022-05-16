package com.example.graduationprojectclient.rv;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationprojectclient.ItemTouchHelperAdapter;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.Suggestion;

import java.util.List;


public class SuggestionRecyclerView extends RecyclerView.Adapter<SuggestionRecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Suggestion> suggestions;

    public SuggestionRecyclerView(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
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
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    @Override
    public void onItemDismiss(int position) {
        suggestions.remove(position);
        notifyItemRemoved(position);
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
