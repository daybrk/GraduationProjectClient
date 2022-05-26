package com.example.graduationprojectclient.rv;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationprojectclient.interfaces.ItemTouchHelperAdapter;
import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.fragments.ViewingAnSuggestion;

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
    public void onBindViewHolder(@NonNull SuggestionRecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.suggestionTheme.setText(suggestions.get(position).getSuggestionTheme());
        holder.suggestionShorText.setText(suggestions.get(position).getSuggestion());
        holder.suggestionDate.setText(suggestions.get(position).getSuggestionDate());
        switch (suggestions.get(position).getSuggestionStatus().getStatus()) {
            case "На рассмотрении":
                holder.suggestionStatus.setText(R.string.on_moderation);
                int colorModeration = holder.itemView.getResources().getColor(R.color.on_moderation);
                holder.suggestionStatus.setTextColor(colorModeration);
                break;
            case "Одобрено":
                holder.suggestionStatus.setText(R.string.accept);
                int colorAccept = holder.itemView.getResources().getColor(R.color.accept);
                holder.suggestionStatus.setTextColor(colorAccept);
                break;
            case "Отклонено":
                holder.suggestionStatus.setText(R.string.canceled);
                int colorCanceled = holder.itemView.getResources().getColor(R.color.canceled);
                holder.suggestionStatus.setTextColor(colorCanceled);
                break;
        }
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
