package com.example.graduationprojectclient.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.graduationprojectclient.entity.Login;

@Dao
public interface LoginDao {

    @Query("SELECT * FROM login")
    Login getLogin();

    @Insert
    void insert(Login login);

    @Update
    void update(Login login);

    @Delete
    void delete(Login login);

}
