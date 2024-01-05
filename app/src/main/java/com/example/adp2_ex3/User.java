package com.example.adp2_ex3;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.ByteArrayOutputStream;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    private String username;
    private String displayName;
    private String profilePic;

    private String token;
    public User(@NonNull String username,String displayName,String profilePic){
        this.username=username;
        this.displayName=displayName;
        this.profilePic=profilePic;
    }


    public Bitmap getProfilePicBitmap() {
        return ImageConverter.base64ToInteger(profilePic);
    }

    public String getProfilePic() {
        return profilePic;
    }

//    public static class BitmapConverter {
//
//        @TypeConverter
//        public static Bitmap fromByteArray(byte[] byteArray) {
//            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        }
//
//        @TypeConverter
//        public static byte[] toByteArray(Bitmap bitmap) {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            return stream.toByteArray();
//        }
//    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static User getUserFromJson(JSONObject obj) throws JSONException {
        String username = obj.getString("username");
        String displayName = obj.getString("displayName");
        String profilePic =  obj.getString("profilePic");
//        Bitmap image = ImageConverter.base64ToInteger(profilePic);
        return new User(username,displayName,profilePic);
    }


}
