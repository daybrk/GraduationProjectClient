package com.example.graduationprojectclient;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.config.ConfigureRetrofit;
import com.example.graduationprojectclient.fragments.UserSuggestions;


public class MainActivity extends AppCompatActivity {

    public static String EMAIL;
    private static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConfigureRetrofit.createRetrofit();
        fm = getSupportFragmentManager();
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
        fragmentTransaction.commit();
    }

    public static FragmentManager getFm() {
        return fm;
    }
}
