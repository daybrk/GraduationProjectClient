package com.example.graduationprojectclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.config.ConfigureRetrofit;
import com.example.graduationprojectclient.fragments.CreateSuggestion;
import com.example.graduationprojectclient.fragments.UserSuggestions;
import com.example.graduationprojectclient.vm.MainViewModel;

import java.util.Observer;


public class MainActivity extends AppCompatActivity {

    final static String checkLoggedIn = "CheckLoggedIn";

    public static String EMAIL;
    private static FragmentManager fm;

    private static Integer isLoggedIn = 0;
    // 0 - Не открывался
    // 1 - После поворота экрана, нужно восстановить
    // 2 - Предложение созданно, работа фрагмента законченна, нормально
    private static Integer suggestionIsOpen = 0;
    private static String suggestionTheme;
    private static String suggestion;

    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConfigureRetrofit.createRetrofit();

        if (isLoggedIn < 2) {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        }

        fm = getSupportFragmentManager();
        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (suggestionIsOpen == 0) {
            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
            fragmentTransaction.commit();
        }
    }

    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(checkLoggedIn, isLoggedIn);
        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isLoggedIn = savedInstanceState.getInt(checkLoggedIn);
        if (suggestionIsOpen == 1) {
            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,
                    new CreateSuggestion(suggestionTheme, suggestion), null);
            fragmentTransaction.commit();
        }
    }

    public static Integer getIsLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(Integer isLoggedIn) {
        MainActivity.isLoggedIn = isLoggedIn;
    }

    public static Integer getSuggestionIsOpen() {
        return suggestionIsOpen;
    }

    public static void setSuggestionIsOpen(Integer suggestionIsOpen) {
        MainActivity.suggestionIsOpen = suggestionIsOpen;
    }

    public static void setSuggestionTheme(String suggestionTheme) {
        MainActivity.suggestionTheme = suggestionTheme;
    }

    public static void setSuggestion(String suggestion) {
        MainActivity.suggestion = suggestion;
    }

    public static FragmentManager getFm() {
        return fm;
    }
}
