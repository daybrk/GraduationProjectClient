package com.example.graduationprojectclient;

import android.os.Bundle;
import android.view.View;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.0.102:8081";

    private TextInputEditText ed_email, ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button but_logIn = (Button) findViewById(R.id.buttonLogin);
        Button but_registration = (Button) findViewById(R.id.buttonRegistration);
        ed_email = (TextInputEditText) findViewById(R.id.user_email);
        ed_password = (TextInputEditText) findViewById(R.id.user_password);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient);
        Retrofit retrofit = retrofitBuilder.build();
        ApiService apiService = retrofit.create(ApiService.class);


        but_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = String.valueOf(ed_email.getText());
                String password = String.valueOf(ed_password.getText());

                UserLogIn user = new UserLogIn(email, password);
                Call<ResponseBody> call = apiService.logIn(user);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            System.out.println(response.message());
                            System.out.println(response.raw());
                            System.out.println(response.body());
                            System.out.println(response.toString());
                            System.out.println(response.headers());
                        } else {
                            System.out.println(response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("HJLPGLHP{FLG{PHL{PGFLH{P");
                        t.printStackTrace();
                    }
                });

            }
        });


    }

}
