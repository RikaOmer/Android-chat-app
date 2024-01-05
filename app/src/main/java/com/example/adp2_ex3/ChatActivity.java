package com.example.adp2_ex3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.adp2_ex3.AppDB;
import com.example.adp2_ex3.Chats.Message;
import com.example.adp2_ex3.Chats.MessageDao;
import com.example.adp2_ex3.ContactsActivity;
import com.example.adp2_ex3.MyApp;
import com.example.adp2_ex3.User;
import com.example.adp2_ex3.adapters.MessageAdapter;
import com.google.gson.JsonObject;

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

public class ChatActivity extends AppCompatActivity {
    private List<Message>messages ;
    private MessageAdapter messageAdapter;
    private String base_url;
    private String Token ;
    private String currentChatId;
    private MessageDao messageDao;
    private RecyclerView listOfMessages;
    private Socket mSocket;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        messages = new ArrayList<>();
        MyApp myApp = ((MyApp) getApplication());
        this.base_url = myApp.getBaseUrl();
        this.Token = myApp.getToken();
        this.currentChatId = myApp.getCurrentChatId();
        String mainUser = myApp.getMainUser().getUsername();
        this.messageAdapter = new MessageAdapter(this, mainUser);
        listOfMessages =  findViewById(R.id.chats_messages_lst);
        listOfMessages.setAdapter(this.messageAdapter);
        listOfMessages.setLayoutManager(new LinearLayoutManager(this));
        loading = findViewById(R.id.chat_loading_indicator);

        AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "messagessssdds")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        messageDao = db.messageDao();
        getMessagesFromRoom();

        getMessages();
        addSwipeRefreshListener();

        try {
            addSendBtnListener();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//
        addBackBtnListener();
        fillOtherUserData();
        try {
            mSocket = IO.socket( ((MyApp)getApplication()).getSocketUrl());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("message_received", args -> {
            try {
                JSONObject data = new JSONObject((String)args[0]);
                if (currentChatId.equals(data.get("chat_id").toString())){
                    getMessages();
                }
            } catch (JSONException e) {
                Toast.makeText(ChatActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mSocket.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessagesFromRoom();
    }

    public void getMessages(){
        OkHttpClient okHttpClient = new OkHttpClient();
        base_url = ((MyApp)getApplication()).getBaseUrl();
//        loading = findViewById(R.id.chat_loading_indicator);
//        loading.setVisibility(View.VISIBLE);
        Request request = new Request.Builder()
                .url(this.base_url+"/api/chats/"+this.currentChatId+"/Messages")
                .addHeader("authorization",this.Token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Server address is unavailable", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        return;

                    }
                    String myResponse = response.body().string();
                    JSONArray c;
                    try {
                        c = new JSONArray(myResponse);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    messages.clear();
                    try {
                        Message.getMessageFromJson(currentChatId,c,messages);
                    }catch (Exception e){
                    }
                    runOnUiThread(() -> {
                        loading.setVisibility(View.INVISIBLE);
                        messageAdapter.setMessages(messages);
                        listOfMessages.scrollToPosition(messages.size()-1);

                    });
                    messageDao.deleteAllByChatId(currentChatId);
                    for (Message m:messages){
                        messageDao.insert(m);
                    }
                }
            }
        });
    }

    private void addSwipeRefreshListener(){
        SwipeRefreshLayout swipeView = findViewById(R.id.chat_swipe_refresh);
        swipeView.setOnRefreshListener(() -> {
            swipeView.setRefreshing(true);
            this.messages.clear();
            List<Message> refreshMessages = new ArrayList<>();
            getMessages();
            this.messageAdapter.setMessages(refreshMessages);
            swipeView.setRefreshing(false);
        });
    }

    private void addSendBtnListener() throws JSONException{
        ImageButton btnSend = findViewById(R.id.chats_btn_send);
        btnSend.setOnClickListener(v -> {
            OkHttpClient okHttpClient = new OkHttpClient();
            JSONObject bodyJson = new JSONObject();
            EditText sendRowInput = findViewById(R.id.chats_edit_text_send);
            String content = sendRowInput.getText().toString();
            sendRowInput.setText("");
            try {
                bodyJson.put("msg",content);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            String mediaType = "application/json";
            RequestBody requestBody = RequestBody.create( bodyJson.toString(), MediaType.parse(mediaType));
            base_url = ((MyApp)getApplication()).getBaseUrl();
            loading.setVisibility(View.VISIBLE);
            Request request = new Request.Builder()
                    .url(this.base_url+"/api/chats/"+currentChatId+"/Messages")
                    .post(requestBody)
                    .addHeader("authorization",this.Token)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Server address is unavailable", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    runOnUiThread(() -> {
                        if (response.isSuccessful()){
                            getMessages();
                            messageAdapter.setMessages(messages);
                            JsonObject chat_id_json_to_socket = new JsonObject();
                            chat_id_json_to_socket.addProperty("chat_id",currentChatId);
                            mSocket.emit("message_sent",chat_id_json_to_socket.toString());
                        }
                        else{
                            Toast.makeText(ChatActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });


    }
    public void addBackBtnListener(){
        ImageButton btnBack = findViewById(R.id.chats_btn_back);
        btnBack.setOnClickListener(v->{
            Intent contact = new Intent(this, ContactsActivity.class);
            startActivity(contact);
        });
    }

    public void fillOtherUserData(){
        User other = ((MyApp) this.getApplication()).getOtherUser();
        ImageView otherProfile = findViewById(R.id.chats_other_user_profile);
        TextView otherName = findViewById(R.id.chat_lbl_name);
        otherProfile.setImageBitmap(other.getProfilePicBitmap());
        otherName.setText(other.getDisplayName());
    }


    public void getMessagesFromRoom(){
        messages.clear();
        List<Message> roomMessages = messageDao.get(currentChatId);
        messageAdapter.setMessages(roomMessages);
        listOfMessages.scrollToPosition(roomMessages.size()-1);
    }


}
