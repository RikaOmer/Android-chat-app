package com.example.adp2_ex3.server;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ServerDao {
    @Query("SELECT * FROM Server Limit 1")
    List<Server> index();

    @Query("DELETE FROM Server")
    void deleteAll();

    @Insert
    void insert(Server... servers);
}
