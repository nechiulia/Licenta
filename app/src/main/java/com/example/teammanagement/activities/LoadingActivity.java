package com.example.teammanagement.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.HttpManager;
import com.example.teammanagement.Utils.User;
import com.google.gson.Gson;

public class LoadingActivity extends AppCompatActivity {
    private ImageView iv_logo;
    private static final String URL = Constants.FAQ_URL;
    private Intent intent;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        iv_logo = findViewById(R.id.loading_iv_logo);
        sharedPreferences=getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);

        Animation tranz = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.loading_logo);
        iv_logo.startAnimation(tranz);
        @SuppressLint("StaticFieldLeak") HttpManager manager=new HttpManager(){
            @Override
            protected void onPostExecute(String s) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.FAQ_LIST_HINT,s);
                editor.apply();
            }

        };
        manager.execute(URL);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }
        };
        timer.start();
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



}
