package com.example.adp2_ex3;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private String baseUrl;
    private String username;
    private MyApp myApp;
    private UserDao userDao;
    private LoadingIndicator loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initVariables();

        checkIfUserConnected();
        addGoToSettingsListener();

    }

    private void initVariables(){
        myApp = (MyApp) getApplication();
        AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "mainUsers")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        userDao = db.userDao();
        baseUrl = myApp.getBaseUrl();
        loadingIndicator = new LoadingIndicator(this);

        Button btn_login = findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(v -> {
            try {
                login();
            }
            catch (Exception e){}
        });

        TextView toRegister = findViewById(R.id.login_text_to_register);
        toRegister.setOnClickListener(v -> {
            Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(register);
            finish();
        });
    }


    public void login() throws Exception{
        Log.d("new", baseUrl);
        TextView usernameTextView = findViewById(R.id.login_edit_username);
        TextView passwordTextView = findViewById(R.id.login_edit_password);
        username = usernameTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        usernameTextView.setText("");
        passwordTextView.setText("");

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("username",username);
        bodyJson.put("password", password);
        String mediaType = "application/json";

        baseUrl = myApp.getBaseUrl();
        String url = baseUrl+"/api/Tokens";
        RequestBody requestBody = RequestBody.create(bodyJson.toString(), MediaType.parse(mediaType));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        loadingIndicator.setDialog(true);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Server address is unavailable", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String myResponse;
                    if (response.body() != null) {
                        myResponse = response.body().string();
                        myApp.setToken("bearer "+myResponse);
                        setMainUserByToken(myApp.getToken());
                    }


                }
                else{
                    loadingIndicator.setDialog(false);
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Username or password are incorrect", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


    public void setMainUserByToken(String token){
        baseUrl = ((MyApp)getApplication()).getBaseUrl();
        String url = baseUrl+"/api/Users/"+username;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("authorization",token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(()->
                Toast.makeText(LoginActivity.this, "Error occurred", Toast.LENGTH_SHORT).show()
                );
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()){
                    loadingIndicator.setDialog(false);
                    return;
                }
                if (response.body() == null) {
                    return;

                }
                String myResponse = response.body().string();
                JsonObject userBody = JsonParser.parseString(myResponse).getAsJsonObject();
                String userUsername = userBody.get("username").getAsString();
                String userDisplay = userBody.get("displayName").getAsString();
                String userProfilePic=userBody.get("profilePic").getAsString();
                User mainUser = new User(userUsername,userDisplay,userProfilePic);
                mainUser.setToken(token);
                myApp.setMainUser(mainUser);
                userDao.deleteAll();
                userDao.insert(mainUser);
                notification();
                loadingIndicator.setDialog(false);
                Intent contact = new Intent(getBaseContext(),ContactsActivity.class);
                startActivity(contact);
                finish();
            }
        });
    }
    public void checkIfUserConnected(){
        List<User> users=userDao.index();
        if (users.size()>0){
            User main = users.get(0);
            myApp.setMainUser(main);
            myApp.setToken(main.getToken());
            goToContact();
        }
    }

    public void goToContact(){
        Intent contact = new Intent(getBaseContext(),ContactsActivity.class);
        startActivity(contact);
        finish();
    }

    public void addGoToSettingsListener(){
        CircleImageView btnLoginSettings = findViewById(R.id.login_settings_icon);
        btnLoginSettings.setOnClickListener(v -> {
            Intent register = new Intent(LoginActivity.this, SettingActivity.class);
            startActivity(register);
        });

    }

        public void notification() {
            String username = myApp.getMainUser().getUsername();
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        // connect to server
                        OkHttpClient okHttpClient = new OkHttpClient();
                        JSONObject bodyJson = new JSONObject();
                        try {
                            bodyJson.put("username", username);
                            bodyJson.put("token", token);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String mediaType = "application/json";
                        String url = baseUrl + "/api/Notification";
                        RequestBody requestBody = RequestBody.create(bodyJson.toString(), MediaType.parse(mediaType));
                        Request request = new Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(()->
                                Toast.makeText(getBaseContext(), "fail", Toast.LENGTH_SHORT).show()
                            );}

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    if (response.isSuccessful()) {
                                        runOnUiThread(() -> myApp.setDeviceToken(token));

                                    }


                            }
                        });
                    });
        }


    }
