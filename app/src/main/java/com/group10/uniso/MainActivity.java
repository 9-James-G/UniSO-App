package com.group10.uniso;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final private static int SPLASH_TIME_OUT=100;
    Button retry;
    ProgressBar retryBar;
//    private static final String SHARED_PREF_NAME= "preference";
//    private static final String KEY_PREF= "my_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retry= findViewById(R.id.retry);
        retryBar= findViewById(R.id.retryBar);
//        rEmail = findViewById(R.id.staffEmail);
//        rPassword = findViewById(R.id.password);
//        fAuth = FirebaseAuth.getInstance();
//        dbReference = FirebaseDatabase.getInstance().getReference("Requests");
//        progressBar = findViewById(R.id.progressBar);
//        rLoginBtn1 = findViewById(R.id.loginBtn1);
//        rcreateBtn = findViewById(R.id.createBtn);

//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Form.PREFS_NAME, 0);
                boolean hasloggedIn = sharedPreferences.getBoolean("hasloggedIn", false);
                boolean hasloggedIn1 = sharedPreferences.getBoolean("hasloggedIn1", false);

                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {
                        if (hasloggedIn) {
                            Intent intent = new Intent(getApplicationContext(), Form.class);
                            startActivity(intent);
                            finish();
                        } else if (hasloggedIn1) {
                            Intent intent = new Intent(getApplicationContext(), AdminSide.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        //no connection
                        retryBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        retry.setVisibility(View.VISIBLE);
                    }

            }
        },SPLASH_TIME_OUT);



    }

    public void retryCon(View view) {
        retryBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Form.PREFS_NAME, 0);
                boolean hasloggedIn = sharedPreferences.getBoolean("hasloggedIn", false);
                boolean hasloggedIn1 = sharedPreferences.getBoolean("hasloggedIn1", false);

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {
                    if (hasloggedIn) {
                        Intent intent = new Intent(getApplicationContext(), Form.class);
                        startActivity(intent);
                        finish();
                    } else if (hasloggedIn1) {
                        Intent intent = new Intent(getApplicationContext(), AdminSide.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    //no connection
                    retryBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        },SPLASH_TIME_OUT);
    }
}
