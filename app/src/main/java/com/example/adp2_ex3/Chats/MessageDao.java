package com.example.adp2_ex3.Chats;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface MessageDao {
    @Query("SELECT * FROM Message")
    List<Message> index();

    @Query("SELECT * FROM Message WHERE chatId = :id")
    List<Message> get(String id);

    @Insert
    void insert(Message... messages);

    @Query("Delete From Message WHERE chatId=:id")
    void deleteAllByChatId(String id);
    @Update
    void update(Message... messages);

    @Delete
    void delete(Message... messages);

}
