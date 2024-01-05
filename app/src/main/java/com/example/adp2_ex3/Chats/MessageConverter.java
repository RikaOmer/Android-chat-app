package com.example.adp2_ex3.Chats;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class MessageConverter {
    @TypeConverter
    public static Message[] fromJsonString(String value) {
        Type listType = new TypeToken<Message[]>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toJsonString(Message[] messages) {
        return new Gson().toJson(messages);
    }
}
