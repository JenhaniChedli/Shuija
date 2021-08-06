package com.example.shu_ija;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class ActivityLogo extends AppCompatActivity {
    TelephonyManager tm;
    String imei;
    ActivitySession activitySession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        int permisI = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        activitySession  = new ActivitySession(this) ;



        if (!activitySession.checkInsertedDevice()) {

            if(permisI == PackageManager.PERMISSION_GRANTED){
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                imei = tm.getDeviceId().toString();
                activitySession.createActivitySession(imei);
               // Toast.makeText(getApplicationContext(), imei, Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},123);
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                imei = tm.getDeviceId().toString();
                activitySession.createActivitySession(imei);
               // Toast.makeText(getApplicationContext(), imei, Toast.LENGTH_LONG).show();
            }



        }
        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(ActivityLogo.this, MenuActivity.class));
                finish();

            }
        },2000);




    }
}



