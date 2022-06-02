package com.example.graduationprojectclient.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.Status;
import com.example.graduationprojectclient.entity.Suggestion;
import com.example.graduationprojectclient.entity.User;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateSuggestion extends Fragment {

    Button createSuggestion;
    EditText suggestionTheme;
    EditText suggestionText;
    TextView charCount;
    TextInputLayout textInputLayout;
    View view;

    public CreateSuggestion() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_create_suggestion, container, false);

        suggestionTheme = view.findViewById(R.id.suggestion_theme_create);
        suggestionText = view.findViewById(R.id.suggestion_text_create);
        charCount = view.findViewById(R.id.tv_char_count);
        textInputLayout = view.findViewById(R.id.tv_text_input_layout);
        createSuggestion = view.findViewById(R.id.suggestion_create_button);

        suggestionText.addTextChangedListener(mTextEditorWatcher);
        createSuggestion.setOnClickListener(view -> {

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df =
                    new SimpleDateFormat
                            ("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(currentTime);

            Suggestion suggestion =
                    new Suggestion
                            (suggestionTheme.getText().toString(), suggestionText.getText().toString(),
                            formattedDate,new Status(0L), new User(LogInActivity.getInstance().getDb().loginDao().getLogin().getEmail()));

            Call<ResponseBody> call2 = CommunicationWithServerService.getApiService().createSuggestion(suggestion);
            call2.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
                        fragmentTransaction.commit();
                    } else {
                        System.out.println(response.errorBody());
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                }
            });
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            charCount.setText(String.valueOf(s.length()));
            if (s.length() == 2040) {
                charCount.setTextColor(ContextCompat.getColor(view.getContext(), R.color.delete));
                textInputLayout.setError("Превышен лимит символов");
                createSuggestion.setClickable(false);
                Toast toast = Toast.makeText(view.getContext(), "Вы превысили количество символов", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                textInputLayout.setError("");
                createSuggestion.setClickable(true);
                charCount.setTextColor(ContextCompat.getColor(view.getContext(), R.color.accept));
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
}