package com.example.graduationprojectclient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.graduationprojectclient.fragments.CreateSuggestion;
import com.example.graduationprojectclient.fragments.ManagingSuggestion;
import com.example.graduationprojectclient.fragments.UserSuggestions;
import com.example.graduationprojectclient.vm.MainViewModel;


public class MainActivity extends AppCompatActivity {

    final static String checkLoggedIn = "CheckLoggedIn";

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

        fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
        if (CommunicationWithServerService.getROLE().equals("USER")) {
            fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, new ManagingSuggestion(), null);
        }
        fragmentTransaction.commit();

        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
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
        //TODO: Переделать
        // Нужно для вызова onRestore
        isLoggedIn = savedInstanceState.getInt(checkLoggedIn);
        if (suggestionIsOpen == 1) {
            FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,
                    new CreateSuggestion(suggestionTheme, suggestion), null);
            fragmentTransaction.commit();
        }
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
