package com.example.graduationprojectclient.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.AuthResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText ed_email, ed_password;
    private Button but_logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        but_logIn = (Button) findViewById(R.id.buttonLogin);
        Button but_registration = (Button) findViewById(R.id.buttonRegistration);
        ed_email = (TextInputEditText) findViewById(R.id.user_email);
        ed_password = (TextInputEditText) findViewById(R.id.user_password);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context context = getApplicationContext();
            Intent intent = new Intent(LogInActivity.this, CommunicationWithServerService.class);
            context.startForegroundService(intent);
        }

        if (CommunicationWithServerService.getEMAIL() == null) {
            but_registration.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                startActivity(intent);
            });

            but_logIn.setClickable(true);
            but_logIn.setOnClickListener(view -> {

                String email = String.valueOf(ed_email.getText());
                String password = String.valueOf(ed_password.getText());

                Call<AuthResponse> call = CommunicationWithServerService.getApiService().logIn(email, password);
                call.enqueue(new Callback<AuthResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                        if (response.isSuccessful()) {
                            but_logIn.setClickable(false);
                            AuthResponse authResponse = response.body();
                            assert authResponse != null;
                            CommunicationWithServerService.setROLE(authResponse.getRole());
                            CommunicationWithServerService.setEMAIL(email);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            try {
                                System.out.println(Objects.requireNonNull(response.errorBody()).string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

            });
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        but_logIn.setClickable(true);
    }
}

