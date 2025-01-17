package com.example.graduationprojectclient.rv;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.fragments.ViewingAnSuggestion;
import com.example.graduationprojectclient.interfaces.ItemTouchHelperAdapter;

import java.util.LinkedList;
import java.util.List;


public class SuggestionRecyclerView extends RecyclerView.Adapter<SuggestionRecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Suggestion> suggestions;
    LinkedList<Suggestion> deletedSuggestion ;

    public SuggestionRecyclerView(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
        deletedSuggestion = new LinkedList<>();
    }

    @NonNull
    @Override
    public SuggestionRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (LogInActivity.getInstance().getDb().loginDao().getLogin().getRole().equals("USER")) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_suggestion_user, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_suggestion, parent, false);
        }
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
        holder.itemView.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ViewingAnSuggestion(suggestions.get(position)), null);
            fragmentTransaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    @Override
    public void onItemDismiss(int position) {
        deletedSuggestion.add(suggestions.get(position));
        suggestions.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemReturned(int position) {
        if (position == 1) {
            suggestions.add(deletedSuggestion.getFirst());
            deletedSuggestion.remove(deletedSuggestion.getFirst());
        } else if (position == 0) {
            suggestions.add(deletedSuggestion.getLast());
            deletedSuggestion.remove(deletedSuggestion.getLast());
        } else if (position == 2) {
            deletedSuggestion.remove(deletedSuggestion.getFirst());
        }
        notifyItemInserted(suggestions.size());
    }

    @Override
    public Suggestion findSuggestionByPosition(int position) {
        return deletedSuggestion.getLast();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView suggestionTheme, suggestionShorText, suggestionDate, suggestionStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            suggestionTheme = itemView.findViewById(R.id.suggestion_theme);
            suggestionShorText = itemView.findViewById(R.id.suggestion_short_text);
            suggestionDate = itemView.findViewById(R.id.suggestion_date);
            suggestionStatus = itemView.findViewById(R.id.suggestion_status);
        }
    }
}
