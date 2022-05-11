package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_suggestion, container, false);

        EditText suggestionTheme = view.findViewById(R.id.suggestion_theme_create);
        EditText suggestionText = view.findViewById(R.id.suggestion_text_create);
        Button createSuggestion = view.findViewById(R.id.suggestion_create_button);

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
                            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
                            fragmentTransaction.commit();
                            System.out.println(MainActivity.getFm());
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
}