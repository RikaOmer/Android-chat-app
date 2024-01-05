package com.example.adp2_ex3.Chats;
import com.example.adp2_ex3.User;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity
@TypeConverters({UserConverter.class, MessageConverter.class, SingleMessageConverter.class})
public class Chat {

    @PrimaryKey
    @NonNull
    String chatId;
    User otherUser;
    Message [] messages;
    Message lastMessage;
    public Chat(String chatId,User otherUser,Message lastMessage){
        this.chatId=chatId;
        this.otherUser=otherUser;
        this.lastMessage=lastMessage;
    }

    public String getChatId() {
        return chatId;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public Message[] getMessages() {
        return messages;
    }

    public User getOtherUser() {
        return otherUser;
    }
    public void setId(String id) {
        this.chatId = id;
    }

}
