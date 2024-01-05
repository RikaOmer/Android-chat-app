package com.example.adp2_ex3.Chats;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {
    @Query("SELECT * FROM Chat")
    List<Chat> index();

    @Query("Select * FROM Chat Where chatId=:id")
    Chat getById(String id);
    @Insert
    void insert(Chat... chats);

    @Update
    void update(Chat... chats);

    @Delete
    void delete(Chat... chats);

    @Query("Delete FROM Chat")
    void deleteAll();
}
