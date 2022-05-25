package com.example.graduationprojectclient;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.fragments.ManagingSuggestion;
import com.example.graduationprojectclient.fragments.UserSuggestions;
import com.example.graduationprojectclient.utilities.CheckOrientation;
import com.example.graduationprojectclient.vm.MainViewModel;


public class MainActivity extends AppCompatActivity {

    private static FragmentManager fm;

    MainViewModel viewModel;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!CheckOrientation.isTabletDevice(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //для портретного режима
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //для альбомного режима
        }
        fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = MainActivity.getFm().beginTransaction();
        if (LogInActivity.getInstance().getDb().loginDao().getLogin().getRole().equals("USER")) {
            fragmentTransaction.replace(R.id.fragment_container, new UserSuggestions(), null);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, new ManagingSuggestion(), null);
        }
        fragmentTransaction.commit();

        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
    }

    public static FragmentManager getFm() {
        return fm;
    }

}
