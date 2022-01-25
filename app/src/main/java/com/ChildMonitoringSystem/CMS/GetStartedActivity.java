package com.ChildMonitoringSystem.CMS;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadCallLogFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadContactFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadMessageFromDevice;
import com.ChildMonitoringSystem.CMS.JobService.AppInfoUploadService;
import com.ChildMonitoringSystem.CMS.JobService.ImageUploadService;
import com.ChildMonitoringSystem.CMS.Model.INFO_CALL_LOG;
import com.ChildMonitoringSystem.CMS.Model.INFO_MESSAGE;
import com.ChildMonitoringSystem.CMS.Model.INFO_PHONEBOOK;
import com.ChildMonitoringSystem.CMS.Service.Service;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetStartedActivity extends AppCompatActivity {
    private static final int JOB_ID = 1;
    private static final int JOB_ID1 = 2;
    private static final int READ_CONTACT_CODE = 10;
    private static final int READ_CALL_LOG_CODE = 11;
    private static final int READ_SMS_CODE = 12;
    private static final int READ_EXTERNAL_STORAGE_CODE = 13;
    private static final int READ_EXTERNAL_STORAGE_CODE1 = 14 ;
    private Button btn1, btn2, btn3, btn4,btn_next,btn5;
    private ImageView img1, img2, img3, img4,img5;
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4,progressBar5;
    private ArrayList<INFO_CALL_LOG> info_call_logs;
    private ArrayList<INFO_PHONEBOOK> info_phonebooks;
    private ArrayList<INFO_MESSAGE> info_messages;
    private LoadCallLogFromDevice loadCallLogFromDevice;
    private LoadContactFromDevice loadContactFromDevice;
    private LoadMessageFromDevice loadMessageFromDevice;




    private final BroadcastReceiver mbroadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constant.ACTION.equals(intent.getAction()))
            {
                progressBar4.setVisibility(View.GONE);
                img4.setImageResource(R.drawable.ic_baseline_check_24);
            }
            if(Constant.ACTION_1.equals(intent.getAction()))
            {
                progressBar5.setVisibility(View.GONE);
                img5.setImageResource(R.drawable.ic_baseline_check_24);
                //LoadInfoAppFromDevice.deleteDirectory(Constant.myDir);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        InitUI();

        btn1.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                loadContactFromDevice = new LoadContactFromDevice();
                info_phonebooks = loadContactFromDevice.Load_Contact(getApplicationContext());
                progressBar1.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SendContacttoServer();
                    }
                }).start();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_CODE);
            }
        });
        btn2.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                loadCallLogFromDevice = new LoadCallLogFromDevice();
                info_call_logs = loadCallLogFromDevice.LoadAllCallLog(getApplicationContext());
                progressBar2.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SendCallLOgtoServer();
                    }
                }).start();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, READ_CALL_LOG_CODE);
            }
        });
        btn3.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                loadMessageFromDevice = new LoadMessageFromDevice();
                info_messages = loadMessageFromDevice.Load_SMS(getApplicationContext());
                progressBar3.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SendMesstoServer();
                    }
                }).start();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, READ_SMS_CODE);
            }
        });
        btn4.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                progressBar4.setVisibility(View.VISIBLE);
                OnstartScheduleJob();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
            }
        });
        btn5.setOnClickListener(v -> {if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            progressBar5.setVisibility(View.VISIBLE);
            OnstartScheduleJob1();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE1);
        }

        });
        btn_next.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CallServiceActivity.class);
            startActivity(intent);
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case READ_CONTACT_CODE:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    loadContactFromDevice = new LoadContactFromDevice();
                    info_phonebooks = loadContactFromDevice.Load_Contact(getApplicationContext());
                    runOnUiThread(() -> {
                        progressBar1.setVisibility(View.VISIBLE);
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendContacttoServer();
                        }
                    }).start();
                }
                else{
                    Toast.makeText(this, "Chức năng không thể sử dụng nếu bạn chưa cấp quyền Truy cập vào danh bạ", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case READ_CALL_LOG_CODE:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    loadCallLogFromDevice = new LoadCallLogFromDevice();
                    info_call_logs = loadCallLogFromDevice.LoadAllCallLog(getApplicationContext());
                    runOnUiThread(() -> progressBar2.setVisibility(View.VISIBLE));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendCallLOgtoServer();
                        }
                    }).start();
                }
                else{
                    Toast.makeText(this, "Chức năng không thể sử dụng nếu bạn chưa cấp quyền Truy cập vào cuộc gọi", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case READ_SMS_CODE:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    loadMessageFromDevice = new LoadMessageFromDevice();
                    info_messages = loadMessageFromDevice.Load_SMS(getApplicationContext());
                    runOnUiThread(()-> progressBar3.setVisibility(View.VISIBLE));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendMesstoServer();
                        }
                    }).start();
                }
                else{
                    Toast.makeText(this, "Chức năng không thể sử dụng nếu bạn chưa cấp quyền Truy cập vào tin nhắn", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case READ_EXTERNAL_STORAGE_CODE:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    runOnUiThread(()-> progressBar4.setVisibility(View.VISIBLE));
                    OnstartScheduleJob();
                }
                else{
                    Toast.makeText(this, "Chức năng không thể sử dụng nếu bạn chưa cấp quyền Truy cập danh mục ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case READ_EXTERNAL_STORAGE_CODE1:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    runOnUiThread(()-> progressBar5.setVisibility(View.VISIBLE));
                    OnstartScheduleJob();
                }
                else{
                    Toast.makeText(this, "Chức năng không thể sử dụng nếu bạn chưa cấp quyền Truy cập bộ nhớ", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    private void InitUI() {
        btn1 = findViewById(R.id.dongbodanhba);
        btn2 = findViewById(R.id.dongbocalllog);
        btn3 = findViewById(R.id.dongbotinnhan);
        btn4 = findViewById(R.id.dongbohinhanh);
        btn5 = findViewById(R.id.dongboapp);
        btn_next = findViewById(R.id.btn_next);
        progressBar1 = findViewById(R.id.progress1);
        progressBar2 = findViewById(R.id.progress2);
        progressBar3 = findViewById(R.id.progress3);
        progressBar4 = findViewById(R.id.progress4);
        progressBar5 = findViewById(R.id.progress5);
        Circle doubleBounce = new Circle();
        Circle doubleBounce1 = new Circle();
        Circle doubleBounce2 = new Circle();
        Circle doubleBounce4 = new Circle();
        Circle doubleBounce5 = new Circle();
        progressBar1.setIndeterminateDrawable(doubleBounce);
        progressBar1.setVisibility(View.INVISIBLE);
        progressBar2.setIndeterminateDrawable(doubleBounce1);
        progressBar2.setVisibility(View.INVISIBLE);
        progressBar3.setIndeterminateDrawable(doubleBounce2);
        progressBar3.setVisibility(View.INVISIBLE);
        progressBar4.setIndeterminateDrawable(doubleBounce4);
        progressBar4.setVisibility(View.INVISIBLE);
        progressBar5.setIndeterminateDrawable(doubleBounce5);
        progressBar5.setVisibility(View.INVISIBLE);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);



    }
    private void  SendContacttoServer()
    {
        if(info_phonebooks.size()>0)
        {
            Service.apiservice.sendPhoneBook(info_phonebooks).enqueue(new Callback<ArrayList<INFO_PHONEBOOK>>() {
                @Override
                public void onResponse(Call<ArrayList<INFO_PHONEBOOK>> call, Response<ArrayList<INFO_PHONEBOOK>> response) {
                    Log.i("TAG", "onResponse: send done");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar1.setVisibility(View.GONE);
                            img1.setImageResource(R.drawable.ic_baseline_check_24);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<INFO_PHONEBOOK>> call, Throwable t) {
                    Log.i("TAG", "onResponse: send failed");
                }
            });


        }
    }
    private void  SendCallLOgtoServer()
    {
        if(info_call_logs.size()>0)
        {
            Service.apiservice.sendCallLog(info_call_logs).enqueue(new Callback<ArrayList<INFO_CALL_LOG>>() {
                @Override
                public void onResponse(Call<ArrayList<INFO_CALL_LOG>> call, Response<ArrayList<INFO_CALL_LOG>> response) {
                    Log.i("TAG", "onResponse: send done"+response.body());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar2.setVisibility(View.GONE);
                            img2.setImageResource(R.drawable.ic_baseline_check_24);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<INFO_CALL_LOG>> call, Throwable t) {
                    Log.i("TAG", "onResponse: send failed");
                }
            });


        }
    }
    private void  SendMesstoServer()
    {
        if(info_messages.size()>0)
        {
            Service.apiservice.sendMessage(info_messages).enqueue(new Callback<ArrayList<INFO_MESSAGE>>() {
                @Override
                public void onResponse(Call<ArrayList<INFO_MESSAGE>> call, Response<ArrayList<INFO_MESSAGE>> response) {
                    Log.i("TAG", "onResponse: send done");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar3.setVisibility(View.GONE);
                            img3.setImageResource(R.drawable.ic_baseline_check_24);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<INFO_MESSAGE>> call, Throwable t) {
                    Log.i("TAG", "onResponse: send failed");
                }
            });


        }
    }


    public void OnstartScheduleJob() {
        ComponentName componentName = new ComponentName(this, ImageUploadService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);

    }
    public void OnstartScheduleJob1() {
        ComponentName componentName = new ComponentName(this, AppInfoUploadService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID1,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "onStart: regis");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION);
        intentFilter.addAction(Constant.ACTION_1);
        registerReceiver(mbroadBroadcastReceiver,intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mbroadBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


