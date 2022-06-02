package com.example.graduationprojectclient.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationprojectclient.AppDataBase;
import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.entity.AuthRegResponse;
import com.example.graduationprojectclient.entity.Login;
import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.utilities.CheckOrientation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private TextInputEditText edEmail, edPassword;
    private TextInputLayout edEmailField, edPasswordField;
    private Button butLogIn, butRegistration;
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

        butLogIn = findViewById(R.id.buttonLogin);
        butRegistration = findViewById(R.id.buttonRegistration);
        edEmail = findViewById(R.id.user_email);
        edPassword = findViewById(R.id.user_password);
        edEmailField = findViewById(R.id.user_email_field);
        edPasswordField = findViewById(R.id.user_password_field);

        edPassword.addTextChangedListener(mTextEditorWatcher);
        edEmail.addTextChangedListener(mTextEditorWatcher);

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

                    Call<AuthRegResponse> call = CommunicationWithServerService.getApiService().logIn(email, password, token);
                    call.enqueue(new Callback<AuthRegResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<AuthRegResponse> call, @NonNull Response<AuthRegResponse> response) {
                            AuthRegResponse authRegResponse = response.body();
                            assert authRegResponse != null;
                            if (authRegResponse.getErrorMessage().equals("")) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast toast = Toast.makeText(context, authRegResponse.getErrorMessage(), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<AuthRegResponse> call, @NonNull Throwable t) {
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

        butRegistration.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
            startActivity(intent);
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    token = task.getResult();
                });

        butLogIn.setOnClickListener(view -> {
            String email = String.valueOf(edEmail.getText());
            String password = String.valueOf(edPassword.getText());

            if (!email.equals("") || !password.equals("")) {

                Call<AuthRegResponse> call = CommunicationWithServerService.getApiService().logIn(email, password, token);
                call.enqueue(new Callback<AuthRegResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<AuthRegResponse> call, @NonNull Response<AuthRegResponse> response) {
                        AuthRegResponse authRegResponse = response.body();
                        assert authRegResponse != null;
                        if (authRegResponse.getErrorMessage().equals("")) {
                            String role = authRegResponse.getRole();
                            db.loginDao().insert(new Login(email, password, role, token));
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast toast = Toast.makeText(context, authRegResponse.getErrorMessage(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AuthRegResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                if (edEmail.length() == 0) {
                    edEmailField.setError("Поле не может быть пустым");
                } if (edPassword.length() == 0) {
                    edPasswordField.setError("Поле не может быть пустым");
                }
                Toast toast = Toast.makeText(context, "Поля email и password - не должны быть пустыми", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (edEmail.length() == 0) {
                edEmailField.setError("Поле не может быть пустым");
            } if (edPassword.length() == 0) {
                edPasswordField.setError("Поле не может быть пустым");
            }

            if (edEmail.length() != 0) {
                edEmailField.setError("");
                edEmailField.setErrorEnabled(false);
            } if (edPassword.length() != 0) {
                edPasswordField.setError("");
                edEmailField.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    public AppDataBase getDb() {
        return db;
    }

    public static LogInActivity getInstance() {
        return instance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        butLogIn.setClickable(true);
    }
}

