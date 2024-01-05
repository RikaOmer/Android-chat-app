package com.example.adp2_ex3;

import com.example.adp2_ex3.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
        @Query("SELECT * FROM users")
        List<User> index();

        @Query("SELECT * FROM users WHERE username = :username")
        User get(int username);

        @Insert
        void insert(User... users);

        @Update
        void update(User... users);

        @Delete
        void delete(User... users);

        @Query("DELETE FROM users")
        void deleteAll();

}
