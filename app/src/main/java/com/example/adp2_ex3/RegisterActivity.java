package com.example.adp2_ex3;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etDisplayName, etPassword, etRedoPassword;
    TextView btnLogin;
    private Bitmap selectedImage;
    String base_url;
    Intent login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.base_url = ((MyApp)getApplication()).getBaseUrl();
        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etDisplayName = findViewById(R.id.etDisplayName);
        etPassword = findViewById(R.id.etPassword);
        etRedoPassword = findViewById(R.id.etRedoPassword);
        Button btnAddPicture = findViewById(R.id.btnAddPicture);
        Button btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        login = new Intent(this, LoginActivity.class);
        btnAddPicture.setOnClickListener(v -> imageChooser());

        btnRegister.setOnClickListener(v -> {
            // Handle register button click
            String username = etUsername.getText().toString().trim();
            String displayName = etDisplayName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String redoPassword = etRedoPassword.getText().toString().trim();

            if (validateInputs(username, displayName, password, redoPassword)) {
                try {
                    createUser(username,displayName,password);
                } catch (Exception e) {
                }
            }
        });
        btnLogin.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });

    }

    private boolean validateInputs(String username, String displayName, String password, String redoPassword) {
        // Perform input validation
        if (selectedImage==null){
            Toast.makeText(this, "Select profile image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.isEmpty() || displayName.isEmpty() || password.isEmpty() || redoPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(redoPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length()<8){
            Toast.makeText(this, "Passwords must contain at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!])[A-Za-z\\d@$!]{8,}$";
        if(password.matches(pattern)){
            return true;
        }else {
            Toast.makeText(this, "Passwords must contain upper case and lowercase characters and digit and one of !@$", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private void imageChooser(){
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 200) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    CircleImageView image = findViewById(R.id.ivPicture);
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    image.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void createUser(String username,String displayName,String password) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("username",username);
        bodyJson.put("password",password);
        bodyJson.put("displayName",displayName);
        bodyJson.put("profilePic",ImageConverter.bitmapToString(selectedImage));
        String mediaType = "application/json";
        base_url = ((MyApp)getApplication()).getBaseUrl();
        String url = this.base_url+"/api/Users";
        LoadingIndicator loadingIndicator = new LoadingIndicator(this);
        loadingIndicator.setDialog(true);
        RequestBody requestBody = RequestBody.create( bodyJson.toString(), MediaType.parse(mediaType));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Server address is unavailable", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                runOnUiThread(()->{
                    if (response.isSuccessful()){
                        loadingIndicator.setDialog(false);
                        Toast.makeText(getBaseContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        // register token for the user and save it
                        startActivity(login);

                    }
                    else{
                        loadingIndicator.setDialog(false);
                        Toast.makeText(getBaseContext(), "User already exists", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
