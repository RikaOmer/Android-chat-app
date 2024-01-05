package com.example.adp2_ex3;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.adp2_ex3.Chats.Chat;
import com.example.adp2_ex3.Chats.ChatDao;
import com.example.adp2_ex3.Chats.Message;
import com.example.adp2_ex3.Chats.MessageDao;
import com.example.adp2_ex3.Chats.UserConverter;
import com.example.adp2_ex3.server.Server;
import com.example.adp2_ex3.server.ServerDao;

@Database(entities = {User.class, Chat.class, Message.class, Server.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract MessageDao messageDao();
    public abstract ChatDao chatDao();

    public abstract ServerDao serverDao();
}
