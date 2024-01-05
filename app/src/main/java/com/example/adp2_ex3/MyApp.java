package com.example.adp2_ex3;

import android.app.Application;

import androidx.room.Room;

import com.example.adp2_ex3.server.Server;
import com.example.adp2_ex3.server.ServerDao;

import java.util.List;

public class MyApp extends Application {

    private User mainUser;
    private User otherUser;
    private String currentChatId;

    private String token;

    private String baseUrl;
    private AppDB db;
    private ServerDao serverDao;
    private String deviceToken;
    public MyApp() {

    }
    public String getCurrentChatId() {
        return currentChatId;
    }

    public User getMainUser() {
        return mainUser;
    }

    public String getToken() {
        return token;
    }

    public String getBaseUrl() {
        return baseUrl+":2500";
    }
    public String getSocketUrl(){
        return baseUrl+":2501";
    }
    public User getOtherUser() {
        return otherUser;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setCurrentChatId(String currentChatId) {
        this.currentChatId = currentChatId;
    }

    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }

    public void setBaseUrl(String baseUrl) {
        serverDao.deleteAll();
        serverDao.insert(new Server(baseUrl));
        this.baseUrl = baseUrl;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void initServer(){
        db = Room.databaseBuilder(getBaseContext(), AppDB.class, "server")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        serverDao = db.serverDao();
        List<Server> Serveress = serverDao.index();
        if (Serveress.size() > 0) {
            setBaseUrl(Serveress.get(0).getUrl());
        } else {
            baseUrl = "http://127.0.0.1";
        }
    }
}