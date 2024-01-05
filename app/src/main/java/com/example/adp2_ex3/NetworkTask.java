package com.example.adp2_ex3;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetworkTask extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... strings) {
        String resp = "";
        String dest_url =strings[0];
        String Token = strings[1];
        String method = strings[2];
        try {
            URL url = new URL(dest_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("authorization",Token);
            int respCode = connection.getResponseCode();
            if (respCode==HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line=reader.readLine())!=null){
                    resp+=line;
                }
                reader.close();

            }else {
                resp="-1";
            }
            return resp;
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
