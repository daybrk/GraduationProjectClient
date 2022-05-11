package com.example.graduationprojectclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.config.ConfigureRetrofit;
import com.example.graduationprojectclient.fragments.UserSuggestions;
import com.example.graduationprojectclient.vm.MainViewModel;

import java.util.Observer;


public class MainActivity extends AppCompatActivity {

    public static String EMAIL;
    private static FragmentManager fm;

    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConfigureRetrofit.createRetrofit();
        fm = getSupportFragmentManager();
        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
        fragmentTransaction.commit();
    }

    public static FragmentManager getFm() {
        return fm;
    }
}
