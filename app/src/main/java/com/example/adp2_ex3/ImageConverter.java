package com.example.adp2_ex3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageConverter {

    public static Bitmap base64ToInteger(String base64Image) {

        // Decode the Base64 string to obtain the byte array
        byte[] imageData = Base64.decode(base64Image.split(",")[1], Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData,0,imageData.length);
        return bitmap;

    }

    public static  String bitmapToString(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        encImage = "data:image/png;base64,"+encImage;
        return encImage;
    }

}