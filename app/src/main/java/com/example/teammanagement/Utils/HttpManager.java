package com.example.teammanagement.Utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpManager extends AsyncTask<String,Void,String> {


    private URL url;
    private HttpURLConnection connection;
    private InputStreamReader inputStreamReader;
    private InputStream inputStream;
    private BufferedReader bufferedReader;

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder stringBuilder=new StringBuilder();
        try {
            url=new URL(strings[0]);
            connection=(HttpURLConnection)url.openConnection();
            inputStream=connection.getInputStream();
            inputStreamReader=new InputStreamReader(inputStream);
            bufferedReader=new BufferedReader(inputStreamReader);

            String linie;
            while((linie=bufferedReader.readLine())!=null){
                stringBuilder.append(linie);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStreamReader!=null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                connection.disconnect();
            }
        }
        return stringBuilder.toString();
    }
}
