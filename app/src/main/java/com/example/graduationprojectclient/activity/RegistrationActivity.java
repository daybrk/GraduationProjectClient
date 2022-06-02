package com.example.graduationprojectclient.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationprojectclient.entity.AuthRegResponse;
import com.example.graduationprojectclient.utilities.CheckOrientation;
import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationActivity extends AppCompatActivity {

    TextInputEditText
            registrationName,
            registrationSecondName,
            registrationLastName,
            registrationEmail,
            registrationPassword;
    TextInputLayout
            registrationNameField,
            registrationSecondNameField,
            registrationLastNameField,
            registrationEmailFiled,
            registrationPasswordField;
    Button buttonSignUp;
    static Context context;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (!CheckOrientation.isTabletDevice(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //для портретного режима
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //для альбомного режима
        }

        context = getApplicationContext();

        registrationName = findViewById(R.id.registration_name);
        registrationSecondName = findViewById(R.id.registration_second_name);
        registrationLastName = findViewById(R.id.registration_last_name);
        registrationEmail = findViewById(R.id.registration_email);
        registrationPassword = findViewById(R.id.registration_password);

        registrationNameField = findViewById(R.id.registration_name_field);
        registrationSecondNameField = findViewById(R.id.registration_second_name_field);
        registrationLastNameField = findViewById(R.id.registration_last_name_field);
        registrationEmailFiled = findViewById(R.id.registration_email_field);
        registrationPasswordField = findViewById(R.id.registration_password_field);

        buttonSignUp = findViewById(R.id.button_sign_up);

        registrationName.addTextChangedListener(mTextEditorWatcher);
        registrationSecondName.addTextChangedListener(mTextEditorWatcher);
        registrationLastName.addTextChangedListener(mTextEditorWatcher);
        registrationEmail.addTextChangedListener(mTextEditorWatcher);
        registrationPassword.addTextChangedListener(mTextEditorWatcher);

        buttonSignUp.setOnClickListener(view -> {
            String email = String.valueOf(registrationEmail.getText());
            String name = String.valueOf(registrationName.getText());
            String secondName = String.valueOf(registrationSecondName.getText());
            String lastName = String.valueOf(registrationLastName.getText());
            String password = String.valueOf(registrationPassword.getText());

            if (!email.equals("") & !name.equals("") & !secondName.equals("") & !password.equals("")) {
                if (!registrationPasswordField.isErrorEnabled() && !registrationNameField.isErrorEnabled()
                        && !registrationSecondNameField.isErrorEnabled() && !registrationEmailFiled.isErrorEnabled()) {

                    User user;
                    if (!lastName.equals("")) {
                        user = new User(email, name, secondName, lastName, password);
                    } else {
                        user = new User(email, name, secondName, password);
                    }

                    Call<AuthRegResponse> call = CommunicationWithServerService.getApiService().createUser(user);
                    call.enqueue(new Callback<AuthRegResponse>() {

                        @Override
                        public void onResponse(@NonNull Call<AuthRegResponse> call, @NonNull Response<AuthRegResponse> response) {
                            AuthRegResponse authRegResponse = response.body();
                            assert authRegResponse != null;
                            if (authRegResponse.getErrorMessage().equals("")) {
                                finish();
                            } else {
                                Toast toast = Toast.makeText(context, authRegResponse.getErrorMessage(), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<AuthRegResponse> call, @NonNull Throwable t) {
                        }
                    });
                }
            } else {
                Toast toast = Toast.makeText(context, "Поля, не должны быть пустыми", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public static Context getContext() {
        return context;
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (registrationName.length() == 0) {
                registrationNameField.setError("Поле не может быть пустым");
            } if (registrationSecondName.length() == 0) {
                registrationSecondNameField.setError("Поле не может быть пустым");
            } if (registrationLastName.length() == 0) {
                registrationLastNameField.setError("Поле не может быть пустым");
            } if (registrationEmail.length() == 0) {
                registrationEmailFiled.setError("Поле не может быть пустым");
            } if (registrationPassword.length() < 6) {
                registrationPasswordField.setError("Пароль должен иметь больше шести символов");
            }

            if (registrationName.length() != 0) {
                registrationNameField.setError("");
                registrationNameField.setErrorEnabled(false);
            }
            if (registrationSecondName.length() != 0) {
                registrationSecondNameField.setError("");
                registrationSecondNameField.setErrorEnabled(false);
            }
            if (registrationLastName.length() != 0) {
                registrationLastNameField.setError("");
                registrationLastNameField.setErrorEnabled(false);
            }
            if (registrationEmail.length() != 0) {
                registrationEmailFiled.setError("");
                registrationEmailFiled.setErrorEnabled(false);
            }
            if (registrationPassword.length() > 6) {
                registrationPasswordField.setError("");
                registrationPasswordField.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}