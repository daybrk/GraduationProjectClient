package com.example.graduationprojectclient.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.graduationprojectclient.AppDataBase;
import com.example.graduationprojectclient.utilities.CheckOrientation;
import com.example.graduationprojectclient.entity.Login;
import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.AuthResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText ed_email, ed_password;
    private Button but_logIn, but_registration;
    private String token;
    private AppDataBase db;
    private static LogInActivity instance;
    Context context;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if (!CheckOrientation.isTabletDevice(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //для портретного режима
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //для альбомного режима
        }

        instance = this;
        db = AppDataBase.getDatabase(instance);

        but_logIn = findViewById(R.id.buttonLogin);
        but_registration = findViewById(R.id.buttonRegistration);
        ed_email = findViewById(R.id.user_email);
        ed_password = findViewById(R.id.user_password);

        context = getApplicationContext();
        context.startService(new Intent(LogInActivity.this, CommunicationWithServerService.class));

        Runnable runnable1 = () -> {
            try {
                if (db.loginDao().getLogin() != null) {
                    String email = db.loginDao().getLogin().getEmail();
                    String password = db.loginDao().getLogin().getPassword();
                    String token = db.loginDao().getLogin().getToken();

                    while (CommunicationWithServerService.getApiService() == null) {
                    }

                    Call<AuthResponse> call = CommunicationWithServerService.getApiService().logIn(email, password, token);
                    call.enqueue(new Callback<AuthResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                            if (response.isSuccessful()) {
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
                }
            } catch (Exception e) {
                Log.e("runnable1", e.getMessage());
            }
        };
        Thread thread1 = new Thread(runnable1);
        thread1.start();

        but_registration.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
            startActivity(intent);
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    token = task.getResult();
                });

        but_logIn.setOnClickListener(view -> {

            String email = String.valueOf(ed_email.getText());
            String password = String.valueOf(ed_password.getText());

            Call<AuthResponse> call = CommunicationWithServerService.getApiService().logIn(email, password, token);
            call.enqueue(new Callback<AuthResponse>() {

                @Override
                public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                    if (response.isSuccessful()) {
                        AuthResponse authResponse = response.body();
                        assert authResponse != null;
                        String role = authResponse.getRole();
                        db.loginDao().insert(new Login(email, password, role, token));

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
    }

    public AppDataBase getDb() {
        return db;
    }

    public static LogInActivity getInstance() {
        return instance;
    }


    @Override
    protected void onResume() {
        super.onResume();
        but_logIn.setClickable(true);
    }
}

