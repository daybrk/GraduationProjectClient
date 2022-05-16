package com.example.graduationprojectclient.activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationprojectclient.CommunicationWithServerService;
import com.example.graduationprojectclient.config.ConfigureRetrofit;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.User;
import com.example.graduationprojectclient.utilities.RegistrationValidator;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationActivity extends AppCompatActivity implements TextWatcher{

    TextInputEditText registrationName, registrationSecondName,
            registrationLastName, registrationEmail, registrationPassword;
    Button buttonSignUp;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context = getApplicationContext();

        registrationName = (TextInputEditText) findViewById(R.id.registration_name);
        registrationSecondName = (TextInputEditText) findViewById(R.id.registration_second_name);
        registrationLastName = (TextInputEditText) findViewById(R.id.registration_last_name);
        registrationEmail = (TextInputEditText) findViewById(R.id.registration_email);
        registrationPassword = (TextInputEditText) findViewById(R.id.registration_password);

        buttonSignUp = (Button) findViewById(R.id.button_sign_up);

        registrationName.addTextChangedListener(this);
        registrationSecondName.addTextChangedListener(this);
        registrationLastName.addTextChangedListener(this);
        registrationEmail.addTextChangedListener(this);
        registrationPassword.addTextChangedListener(this);

        buttonSignUp.setOnClickListener(view -> {
            String email = String.valueOf(registrationEmail.getText());
            String name = String.valueOf(registrationName.getText());
            String secondName = String.valueOf(registrationSecondName.getText());
            String lastName = String.valueOf(registrationLastName.getText());
            String password = String.valueOf(registrationPassword.getText());

            if (RegistrationValidator.Validator(email, name, secondName, password)) {
                User user;
                if (!lastName.equals("")) {
                     user = new User(email, name, secondName, lastName, password, 3);
                } else {
                     user = new User(email, name, secondName, password, 3);
                }
                Call<ResponseBody> call = CommunicationWithServerService.getApiService().createUser(user);
                call.enqueue(new Callback<ResponseBody>() {

                     @Override
                     public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                         if (response.isSuccessful()) {
                             System.out.println(response.body() + " TUTTAA");
                             finish();
                         } else {
                             System.out.println(response.errorBody());
                         }
                     }
                     @Override
                     public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                     }
                 });
            }
        });
    }



    public static Context getContext() {
        return context;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        if (registrationName.length() > start) {
            editTextValidator(registrationName);
        }
        if (registrationSecondName.length() > start) {
            editTextValidator(registrationSecondName);
        }
        if (registrationLastName.length() > start) {
            editTextValidator(registrationLastName);
        }
        if (registrationEmail.length() > start) {
            editTextValidator(registrationEmail);
        }
        if (registrationPassword.length() > start) {
            editTextValidator(registrationPassword);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    public void editTextValidator(TextInputEditText editText) {
        if (editText.length() <= 1) {
            editText
                    .getBackground()
                    .setColorFilter(getResources()
                                    .getColor(R.color.design_default_color_error),
                            PorterDuff.Mode.SRC_IN);
        } else if (editText.length() >= 1) {
            editText
                    .getBackground()
                    .setColorFilter(getResources()
                                    .getColor(R.color.colorAccent),
                            PorterDuff.Mode.SRC_IN);
        }
    }
}