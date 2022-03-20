package com.example.graduationprojectclient;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationprojectclient.entity.User;
import com.example.graduationprojectclient.entity.UserLogIn;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText ed_email, ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button but_logIn = (Button) findViewById(R.id.buttonLogin);
        Button but_registration = (Button) findViewById(R.id.buttonRegistration);
        ed_email = (TextInputEditText) findViewById(R.id.user_email);
        ed_password = (TextInputEditText) findViewById(R.id.user_password);

        ConfigureRetrofit.createRetrofit();

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

//                Call<String> call = ConfigureRetrofit.getApiService().logIn(email, password);
                Call<List<User>> call = ConfigureRetrofit.getApiService().getUsers();
                call.enqueue(new Callback<List<User>>() {

                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
//                            System.out.println(call);
                            for (int i = 0; i < response.body().size(); i++) {
                                System.out.println(response.body().get(i).getEmail());
                            }
                        } else {
                            System.out.println(response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });

    }



}
