package com.example.adp2_ex3.Chats;

import android.util.Log;

import com.example.adp2_ex3.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


@Entity
@TypeConverters({UserConverter.class})
public class Message {
    @PrimaryKey(autoGenerate = true)
     int  id;
    @NonNull
    String chatId;
    String created;
    User sender;
    String content;

    public Message(String chatId,String created,User sender,String content){
        this.chatId=chatId;
        this.created=created;
        this.sender=sender;
        this.content=content;
    }

    public String getChatId() {
        return chatId;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }

    public User getSender() {
        return sender;
    }
    public void setId(String id) {
        this.chatId = id;
    }


    public static List<Message> getMessageFromJson(String chatId,JSONArray obj, List<Message> messages) throws JSONException, ParseException {

        for (int i=0;i<obj.length();i++){
            JSONObject j = obj.getJSONObject(i);
            String content = j.getString("content");
            String created = j.getString("created");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("HH:mm");
            Date d = sdf.parse(created);
            String formattedTime = output.format(d);
            JSONObject senderJson =  j.getJSONObject("sender");
            User sender = User.getUserFromJson(senderJson);
            messages.add(new Message(chatId,formattedTime,sender,content));
        }
        return messages;

    }

    public static Message getMessageFromJson(String chatId,JSONObject obj) throws JSONException, ParseException {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = dateformat.format(c.getTime());

        String content = obj.getString("content");
        String created = obj.getString("created");
        JSONObject senderJson = obj.getJSONObject("sender");
        User sender = User.getUserFromJson(senderJson);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");
        Date d = sdf.parse(created);
        String day = dateformat.format(d);
        String formattedTime = output.format(d);
        if (day.equals(date)){
            return new Message(chatId,formattedTime,sender,content);
        } else {
            return new Message(chatId,day,sender,content);
        }

    }
}
