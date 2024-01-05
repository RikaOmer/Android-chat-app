package com.example.adp2_ex3.Chats;
import androidx.room.TypeConverter;

import com.example.adp2_ex3.User;
import com.google.gson.Gson;

public class UserConverter {
    @TypeConverter
    public static User fromJsonString(String value) {
        return new Gson().fromJson(value, User.class);
    }

    @TypeConverter
    public static String toJsonString(User user) {
        return new Gson().toJson(user);
    }
}