package com.example.shu_ija;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;


public class ActivitySession extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "device";
    private static final String device = "device";
    public static final String imei = "imei";

    public ActivitySession (Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }



    public void createActivitySession(String imei) {
        editor.putBoolean(this.device,true).commit(); ;
        editor.putString(this.imei, imei).commit();;
        editor.apply();
    }

    public boolean checkInsertedDevice(){
        return sharedPreferences.getBoolean(device, false);
    }

    public String getStringImei() {
      return sharedPreferences.getString("imei", null);
    }
}