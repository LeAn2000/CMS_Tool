package com.ChildMonitoringSystem.CMS;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.ChildMonitoringSystem.CMS.BroadCast.Restarter_Service;
import com.ChildMonitoringSystem.CMS.JobService.AudioService;
import com.ChildMonitoringSystem.CMS.JobService.LocationService;
import com.ChildMonitoringSystem.CMS.JobService.UploadDataService;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.WorkManager.SendCallLogWork;

import java.util.concurrent.TimeUnit;

public class CallServiceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG_SEND_DATA = "senddata";
    private Button btn, btn_next;
    private LinearLayout linearLayout;
    private Spinner spin;
    private EditText edit;
    private String getTime = "NGÀY";
    String[] times = {"NGÀY", "GIỜ", "PHÚT",};
    Intent mServiceIntent, intent;
    private AudioService audioService;
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_service);
        InitUI();
        btn.setOnClickListener(v -> {
            String edittext = edit.getText().toString();
            if (edittext.equals("")) {
                Toast.makeText(CallServiceActivity.this, "Bạn phải chọn thời gian đồng bộ", Toast.LENGTH_SHORT).show();
            } else {
                Long time = Long.parseLong(edittext);
                TimeUnit unit = Gettimeunit(getTime);
                SettingUpPeriodicWork(time, unit);
                Toast.makeText(CallServiceActivity.this, "Chọn thời gian đồng bộ dữ liệu thành công", Toast.LENGTH_SHORT).show();
            }
        });
        btn_next.setOnClickListener(v -> {
            Intent inten = new Intent(getApplicationContext(), LastStepActivity.class);
            startActivity(inten);
        });

        RunMyService();

//        btn_audio.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//                if (!isMyServiceRunning(audioService.getClass())) {
//                    startService(mServiceIntent);
//                    audio = true;
//                    count_service++;
//                    Toast.makeText(this, "Ghi âm cuộc gọi đã được kích hoạt", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Dịch vụ đang chạy2", Toast.LENGTH_SHORT).show();
//                }
//
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_CODE);
//            }
//        });
//        btn_location.setOnClickListener(v -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    if (!isMyServiceRunning(locationService.getClass())) {
//                        startService(intent);
//                        location = true;
//                        count_service++;
//                        Toast.makeText(this, "Theo dõi vị trí đã được kích hoạt", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Dịch vụ đang chạy1", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, LOCATION_CODE);
//                }
//            } else {
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    if (!isMyServiceRunning(locationService.getClass())) {
//                        startService(intent);
//                        location = true;
//                        count_service++;
//                        Toast.makeText(this, "Theo dõi vị trí đã được kích hoạt", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Dịch vụ đang chạy1", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
//                }
//            }
//
//        });
    }

    private void RunMyService()
    {
        if (!isMyServiceRunning(audioService.getClass())) {
            startService(mServiceIntent);
        }
        if (!isMyServiceRunning(locationService.getClass())) {
            startService(intent);
        }
    }
    private void InitUI() {
        btn = findViewById(R.id.button);
        btn_next = findViewById(R.id.btn_next_callservice);
        linearLayout = findViewById(R.id.ghichu);
        linearLayout.setOnClickListener(v -> {
            OpenInforDialog(Gravity.CENTER);
        });
        spin = findViewById(R.id.timespinner);
        edit = findViewById(R.id.edit_time);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        audioService = new AudioService();
        locationService = new LocationService();
        intent = new Intent(this, locationService.getClass());
        mServiceIntent = new Intent(this, audioService.getClass());
    }

    private void OpenInforDialog(int center) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = center;
        window.setAttributes(windowLayoutParams);

        if (Gravity.CENTER == center) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        Button btn_dialog_OK = dialog.findViewById(R.id.btn_dialog);
        btn_dialog_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private TimeUnit Gettimeunit(String timer) {
        if (timer.equals("NGÀY")) {
            return TimeUnit.DAYS;
        } else if (timer.equals("GIỜ")) {
            return TimeUnit.HOURS;
        } else {
            return TimeUnit.MINUTES;
        }

    }

    private void SettingUpPeriodicWork(Long time, TimeUnit timeUnit) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build();
        PeriodicWorkRequest periodicSendDataWork =
                new PeriodicWorkRequest.Builder(SendCallLogWork.class, time, timeUnit)
                        .setInitialDelay(15,TimeUnit.MINUTES)
                        .addTag(TAG_SEND_DATA)
                        .setConstraints(constraints)
                        .build();
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueue(periodicSendDataWork);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent broadcast = new Intent();
        broadcast.setAction(Constant.ALL_SERVICE);
        broadcast.setClass(this, Restarter_Service.class);
        this.sendBroadcast(broadcast);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String timer = edit.getText().toString();
        if (!timer.equals("")) {
            int time = Integer.parseInt(edit.getText().toString());
            if (times[position].equals("NGÀY")) {
                getTime = times[position];
                edit.setText("1");
            } else if (times[position].equals("GIỜ")) {
                getTime = times[position];
                if (time > 24) {
                    edit.setText("24");
                } else if (time <= 0) {
                    edit.setText("1");
                }

            } else {
                getTime = times[position];
                if (time > 60) {
                    edit.setText("60");
                } else if (time < 15) {
                    edit.setText("15");
                }

            }
        } else {
            Toast.makeText(this, "Bạn phải điền thời gian vào ô trống", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}