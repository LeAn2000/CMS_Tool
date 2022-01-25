package com.ChildMonitoringSystem.CMS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import com.ChildMonitoringSystem.CMS.Model.MySharereference;
import com.ChildMonitoringSystem.CMS.Value.Constant;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        MySharereference mySharereference = new MySharereference(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mySharereference.getValue(Constant.KEY_FIRST)) {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    finish();
                } else {
                    startActivity(LoginActivity.class);
                    mySharereference.putValue(Constant.KEY_FIRST, true);
                }
            }
        }, 2000);
    }

    private void startActivity(Class<?> myclass) {
        Intent intent = new Intent(this, myclass);
        startActivity(intent);
        finish();
    }
}