package com.example.graduationprojectclient;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationprojectclient.activitys.LogInActivity;
import com.example.graduationprojectclient.config.ConfigureRetrofit;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConfigureRetrofit.createRetrofit();

        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);
    }

}
