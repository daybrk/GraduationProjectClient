package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.config.ConfigureRetrofit;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.entity.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateSuggestion extends Fragment {

    EditText suggestionTheme;
    EditText suggestionText;
    String restoreSuggestionTheme;
    String restoreSuggestionText;

    public CreateSuggestion(String suggestionTheme, String suggestionText) {
        this.restoreSuggestionTheme = suggestionTheme;
        this.restoreSuggestionText = suggestionText;
    }

    public CreateSuggestion() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_suggestion, container, false);

        suggestionTheme = view.findViewById(R.id.suggestion_theme_create);
        suggestionText = view.findViewById(R.id.suggestion_text_create);
        Button createSuggestion = view.findViewById(R.id.suggestion_create_button);

        if (MainActivity.getSuggestionIsOpen() == 1) {
            suggestionTheme.setText(restoreSuggestionTheme);
            suggestionText.setText(restoreSuggestionText);
        }


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity.setSuggestionIsOpen(0);
                FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        createSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat df =
                        new SimpleDateFormat
                                ("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(currentTime);

                Suggestion suggestion =
                        new Suggestion
                                (suggestionTheme.getText().toString(), suggestionText.getText().toString(),
                                formattedDate, "1", new User(MainActivity.EMAIL));

                Call<ResponseBody> call2 = ConfigureRetrofit.getApiService().createSuggestion(suggestion);
                call2.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            MainActivity.setSuggestionIsOpen(2);
                            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
                            fragmentTransaction.commit();
                        } else {
                            System.out.println(response.errorBody());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("Desttttr", "tut");
        super.onDestroy();
        if (MainActivity.getSuggestionIsOpen() < 2) {
            MainActivity.setSuggestionIsOpen(1);
            MainActivity.setSuggestionTheme(String.valueOf(suggestionTheme.getText()));
            MainActivity.setSuggestion(String.valueOf(suggestionText.getText()));
        } else {
            MainActivity.setSuggestionIsOpen(0);
        }
        super.onSaveInstanceState(outState);
    }



}