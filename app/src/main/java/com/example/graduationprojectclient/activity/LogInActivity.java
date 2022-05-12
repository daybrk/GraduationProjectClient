package com.example.graduationprojectclient.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.config.ConfigureRetrofit;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText ed_email, ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button but_logIn = (Button) findViewById(R.id.buttonLogin);
        Button but_registration = (Button) findViewById(R.id.buttonRegistration);
        ed_email = (TextInputEditText) findViewById(R.id.user_email);
        ed_password = (TextInputEditText) findViewById(R.id.user_password);

        but_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        but_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = String.valueOf(ed_email.getText());
                String password = String.valueOf(ed_password.getText());

                Call<String> call = ConfigureRetrofit.getApiService().logIn(email, password);
                call.enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            MainActivity.EMAIL = email;
                            MainActivity.setIsLoggedIn(2);
                            finish();
                        } else {
                            try {
                                System.out.println(Objects.requireNonNull(response.errorBody()).string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });

    }

}

