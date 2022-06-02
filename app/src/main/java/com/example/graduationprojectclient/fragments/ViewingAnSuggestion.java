package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewingAnSuggestion extends Fragment {

    Suggestion suggestionData;
    Snackbar snackbar;

    public ViewingAnSuggestion(Suggestion suggestion) {
        this.suggestionData = suggestion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewing_an_sugg, container, false);

        TextView suggestionTheme = view.findViewById(R.id.tv_theme);
        EditText suggestion = view.findViewById(R.id.tv_text);
        TextView suggestionDate = view.findViewById(R.id.tv_suggestion_date);
        TextView suggestionAuthor = view.findViewById(R.id.tv_suggestion_author);
        TextView suggestionAccept = view.findViewById(R.id.tv_accept);
        TextView suggestionCanceled = view.findViewById(R.id.tv_canceled);

        suggestionTheme.setText(suggestionData.getSuggestionTheme());
        suggestion.setText(suggestionData.getSuggestion());
        suggestionDate.setText(suggestionData.getSuggestionDate());
        String name = suggestionData.getSuggestionAuthor().getSecondName() + " " + suggestionData.getSuggestionAuthor().getName();
        suggestionAuthor.setText(name);

        if (LogInActivity.getInstance().getDb().loginDao().getLogin().getRole().equals("USER")) {
            TextInputLayout suggestionHint = view.findViewById(R.id.tv_text_field);
            suggestionHint.setHint(R.string.suggestion_3);
            suggestionAccept.setVisibility(View.INVISIBLE);
            suggestionCanceled.setClickable(false);
            switch (suggestionData.getSuggestionStatus().getStatus()) {
                case "На рассмотрении":
                    int colorModeration = view.getResources().getColor(R.color.on_moderation);
                    suggestionCanceled.setText(R.string.on_moderation_text);
                    suggestionCanceled.setTextColor(colorModeration);
                    break;
                case "Одобрено":
                    int colorAccept = view.getResources().getColor(R.color.accept);
                    suggestionCanceled.setText(R.string.accept_text_user);
                    suggestionCanceled.setTextColor(colorAccept);
                    break;
                case "Отклонено":
                    int colorCanceled = view.getResources().getColor(R.color.canceled);
                    suggestionCanceled.setText(R.string.canceled_text_user);
                    suggestionCanceled.setTextColor(colorCanceled);
                    break;
            }
        } else {
            suggestionAccept.setVisibility(View.VISIBLE);
            suggestionCanceled.setVisibility(View.VISIBLE);
        }

        suggestionAccept.setOnClickListener(v -> {
            snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
            snackbar.setAction("Отменить одобрение", view1 -> {
            }).show();
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        Call<ResponseBody> call = CommunicationWithServerService.getApiService()
                                .confirmSuggestion(suggestionData.getSuggestionId(),
                                        LogInActivity.getInstance().getDb().loginDao().getLogin().getEmail());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                Toast toast = Toast.makeText(v.getContext(), "Предложение было принято!", Toast.LENGTH_SHORT);
                                toast.show();

                                FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new ManagingSuggestion(), null);
                                fragmentTransaction.commit();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }
            });
        });


        suggestionCanceled.setOnClickListener(v -> {
            snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
            snackbar.setAction("Вернуть предложение", view1 -> {
            }).show();
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        Call<ResponseBody> call = CommunicationWithServerService.getApiService()
                                .canceledSuggestion(suggestionData.getSuggestionId(),
                                        LogInActivity.getInstance().getDb().loginDao().getLogin().getEmail());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                Toast toast = Toast.makeText(v.getContext(), "Предложение было отклонено!", Toast.LENGTH_SHORT);
                                toast.show();

                                FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new ManagingSuggestion(), null);
                                fragmentTransaction.commit();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }
            });
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                if (LogInActivity.getInstance().getDb().loginDao().getLogin().getRole().equals("USER")) {
                    fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
                } else {
                    fragmentTransaction.replace(R.id.fragment_container, new ManagingSuggestion(), null);
                }
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }


}