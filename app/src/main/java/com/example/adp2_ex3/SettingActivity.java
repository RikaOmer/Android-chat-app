package com.example.adp2_ex3;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity {
    MyApp myApp;
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initVariables();
        initUI();
    }

    private void initVariables(){
        myApp = ((MyApp) getApplication());
    }
    private void initUI(){
        try {
            Button btnLogin = findViewById(R.id.settings_btn_login);
            btnLogin.setVisibility(View.GONE);
            ConstraintLayout mainUserLayout = findViewById(R.id.settings_main_user_layout);
            mainUserLayout.setVisibility(View.VISIBLE);

            ImageView userProfile = findViewById(R.id.settings_user_profile_pic);
            userProfile.setImageBitmap(myApp.getMainUser().getProfilePicBitmap());
            TextView usernameText =findViewById(R.id.settings_text_username);
            usernameText.setText("@"+myApp.getMainUser().getUsername());
            TextView displayText = findViewById(R.id.settings_text_display);
            displayText.setText(myApp.getMainUser().getDisplayName());
        }
        catch (Exception e){
            Button btnLogin = findViewById(R.id.settings_btn_login);
            btnLogin.setVisibility(View.VISIBLE);
            ConstraintLayout mainUserLayout = findViewById(R.id.settings_main_user_layout);
            mainUserLayout.setVisibility(View.GONE);
        }
    }
    public void updateServer(View view) {
        TextView editTextNewServer = findViewById(R.id.settings_server_edit_text);
        myApp.setBaseUrl(editTextNewServer.getText().toString());
        Toast.makeText(SettingActivity.this, "Server Address Updated Successfully", Toast.LENGTH_SHORT).show();
    }
    public void n(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        recreate();
    }
    public void d(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recreate();
    }

    public void logout(View view){
        String currentUser = myApp.getMainUser().getUsername();
        deleteTokenFromServer(currentUser);
        AppDB dbUser = Room.databaseBuilder(getApplicationContext(),AppDB.class,"mainUsers")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        dbUser.userDao().deleteAll();
        myApp.setMainUser(null);
        Intent login = new Intent(this,LoginActivity.class);
        startActivity(login);
        finish();

    }
    public void notifyPermission(View view){

            // This is only necessary for API level >= 33 (TIRAMISU)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SettingActivity.this, "Notification activated", Toast.LENGTH_SHORT).show();
                } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    Toast.makeText(SettingActivity.this, "Notification not activated", Toast.LENGTH_SHORT).show();
                } else {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }

    public  void appInfo(View view){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public void goToLogin(View view){
        Intent login = new Intent(this,LoginActivity.class);
        startActivity(login);
        finish();
    }

    public void back(View view){
        onBackPressed();
    }

    private void deleteTokenFromServer(String username){
        String token = myApp.getDeviceToken();
        if (token==null) return;
        Log.d("token", token);
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject bodyJson = new JSONObject();
        try {
            bodyJson.put("username", username);
            bodyJson.put("token", token);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String mediaType = "application/json";
        String url = myApp.getBaseUrl() + "/api/Notification";
        RequestBody requestBody = RequestBody.create(bodyJson.toString(), MediaType.parse(mediaType));
        Request request = new Request.Builder()
                .url(url)
                .delete(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            }
        });
    }
}