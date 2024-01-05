package com.example.adp2_ex3;

import static com.example.adp2_ex3.Chats.Message.getMessageFromJson;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.adp2_ex3.Chats.Chat;
import com.example.adp2_ex3.Chats.ChatDao;
import com.example.adp2_ex3.Chats.Message;
import com.example.adp2_ex3.SelectListener.SelectListener;
import com.example.adp2_ex3.adapters.ContactAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContactsActivity extends AppCompatActivity implements SelectListener {
    List<Chat> chats;
    String base_url;
    String token;
    ContactAdapter contactAdapter;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private AppDB db;
    private ChatDao chatDao;
    private RecyclerView lstContacts;
    private Socket mSocket;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        this.base_url = ((MyApp)getApplication()).getBaseUrl();
        this.token = ((MyApp) this.getApplication()).getToken();
        this.chats = new ArrayList<>();
        this.lstContacts=   findViewById(R.id.lstContacts);

        db = Room.databaseBuilder(getApplicationContext(),AppDB.class,"contacts"+ ((MyApp)this.getApplication()).getMainUser().getUsername())
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        chatDao = db.chatDao();


        contactAdapter= new ContactAdapter(getBaseContext(),this);

        lstContacts.setAdapter(contactAdapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));
        getChatsFromRoom();

        getContacts();
        fillMainUser();
        addAddUserListener();
        addSearchListener();
        addSwipeRefreshListener();

        try {
            mSocket = IO.socket( ((MyApp)getApplication()).getSocketUrl());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("message_received", args -> {
            try {
                JSONObject data = new JSONObject((String)args[0]);
                for(Chat current:chats){
                    if (current.getChatId().equals(data.get("chat_id").toString())){
                        getContacts();
                    }
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        mSocket.connect();
        //checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChatsFromRoom();
    }

    public User getUserFromJson(JSONObject obj) throws JSONException {
        String username = obj.getString("username");
        String displayName = obj.getString("displayName");
        String profilePic =  obj.getString("profilePic");
        return new User(username,displayName,profilePic);
    }


    public void getContacts(){
        base_url = ((MyApp)getApplication()).getBaseUrl();
        String url = this.base_url+"/api/chats";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("authorization",this.token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ContactsActivity.this, "Server address is unavailable", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()){
                    goToLogin();
                    return;
                }
                String myResponse = response.body().string();
                JSONArray c = null;
                try {
                    c = new JSONArray(myResponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                int len = c.length();
                chats.clear();
                chatDao.deleteAll();
                for (int i=0;i<len;i++) {
                    try {
                        JSONObject object = c.getJSONObject(i);
                        User u = getUserFromJson(object.getJSONObject("user"));
                        Message message=null;
                        String chatId = object.getString("id");
                        try {
                            message = getMessageFromJson(chatId,object.getJSONObject("lastMessage"));
                        }catch (Exception e){
                            message = new Message("0","",u,"");
                        }

                        Chat chat = new Chat(chatId,u,message);
                        chats.add(chat);
                        chatDao.insert(chat);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ContactsActivity.this.runOnUiThread(() -> {
                    contactAdapter.setChats(chats);
                });

            }
        });
    }
    public void addUser() throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        TextView searchBar = findViewById(R.id.search_text);
        String newUser = searchBar.getText().toString();
        searchBar.setText("");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("username",newUser);
        String mediaType = "application/json";
        base_url = ((MyApp)getApplication()).getBaseUrl();
        String url = base_url+"/api/chats";
        RequestBody requestBody = RequestBody.create( bodyJson.toString(), MediaType.parse(mediaType));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("authorization",this.token)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ContactsActivity.this, "Server address is unavailable", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                Toast.makeText(ContactsActivity.this, "Chat has been created", Toast.LENGTH_SHORT).show();
                                getContacts();
                            }
                            else{
                                Toast.makeText(ContactsActivity.this, "Username was not found / Chat is already exists", Toast.LENGTH_SHORT).show();

                            };
                        }
                    });
                }
        });
    }



    public void addAddUserListener(){
        Button addButton = findViewById(R.id.btn_add_user);
        addButton.setOnClickListener(v -> {
            try {
                addUser();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onItemClicked(Chat chat) {
        ((MyApp) this.getApplication()).setCurrentChatId(chat.getChatId());
        ((MyApp) this.getApplication()).setOtherUser(chat.getOtherUser());
        Intent chatActivity = new Intent(this,ChatActivity.class);
        startActivity(chatActivity);
    }
    public void fillMainUser(){
        User main = ((MyApp) this.getApplication()).getMainUser();
        ImageView mainUserprofile = findViewById(R.id.contact_main_user_profile_pic);
        mainUserprofile.setImageBitmap(main.getProfilePicBitmap());
        TextView mainUserUsername = findViewById(R.id.contact_main_user_username);
        mainUserUsername.setText("@"+main.getUsername());
        TextView mainUserDisplay = findViewById(R.id.contact_main_user_display);
        mainUserDisplay.setText(main.getDisplayName());
    }

    public void  addSearchListener(){
        EditText searchTextBox = findViewById(R.id.contact_search_user_text);
        searchTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().isEmpty()){
                    contactAdapter.setChats(chats);
                }
                else{
                    filter(s.toString());
                }
            }
        });
    }

    public void filter(String filterContent) {
        List<Chat> filteredList = new ArrayList<>();
        for (Chat chat : chats) {
            if (chat.getOtherUser().getDisplayName().toLowerCase().startsWith(filterContent.toLowerCase())) {
                filteredList.add(chat);
            }
        }
        contactAdapter.setChats(filteredList);
    }
    public void getChatsFromRoom(){
        chats=chatDao.index();
        contactAdapter.setChats(chats);
    }

    public void goToLogin(){
        Intent register = new Intent(this, LoginActivity.class);
        startActivity(register);
        this.finish();
    }

    public void goToSettings(View view){
        Intent register = new Intent(this, SettingActivity.class);
        startActivity(register);
    }
    public void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Permission not granted, request it from the user
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_CODE);
        }

    }

    private void addSwipeRefreshListener(){
        SwipeRefreshLayout swipeView =  findViewById(R.id.contact_swipe_refresh);
        swipeView.setOnRefreshListener(() -> {
            swipeView.setRefreshing(true);
            getContacts();
            swipeView.setRefreshing(false);
        });
    }

}