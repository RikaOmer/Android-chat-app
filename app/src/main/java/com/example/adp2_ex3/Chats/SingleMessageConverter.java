package com.example.adp2_ex3.Chats;
import androidx.room.TypeConverter;
import com.google.gson.Gson;

public class SingleMessageConverter {

    @TypeConverter
    public static Message fromJsonString(String value) {
        return new Gson().fromJson(value, Message.class);
    }

    @TypeConverter
    public static String toJsonString(Message message) {
        return new Gson().toJson(message);
    }
}

