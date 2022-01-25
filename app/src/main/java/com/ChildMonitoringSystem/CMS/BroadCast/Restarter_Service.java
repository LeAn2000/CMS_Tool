package com.ChildMonitoringSystem.CMS.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.ChildMonitoringSystem.CMS.JobService.AppInfoUploadService;
import com.ChildMonitoringSystem.CMS.JobService.AudioService;
import com.ChildMonitoringSystem.CMS.JobService.ImageUploadService;
import com.ChildMonitoringSystem.CMS.JobService.LocationService;
import com.ChildMonitoringSystem.CMS.JobService.UploadCallLogDataService;
import com.ChildMonitoringSystem.CMS.JobService.UploadDataService;
import com.ChildMonitoringSystem.CMS.JobService.UploadMessDataService;
import com.ChildMonitoringSystem.CMS.JobService.VideoUploadService;
import com.ChildMonitoringSystem.CMS.Value.Constant;

public class Restarter_Service extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, AudioService.class));
            context.startForegroundService(new Intent(context, LocationService.class));
            context.startForegroundService(new Intent(context, UploadDataService.class));
            context.startForegroundService(new Intent(context, UploadCallLogDataService.class));
            context.startForegroundService(new Intent(context, UploadMessDataService.class));
            context.startForegroundService(new Intent(context, ImageUploadService.class));
            context.startForegroundService(new Intent(context, AppInfoUploadService.class));
            context.startForegroundService(new Intent(context, VideoUploadService.class));
        } else {
            context.startService(new Intent(context, AudioService.class));
            context.startService(new Intent(context, LocationService.class));
            context.startService(new Intent(context, UploadDataService.class));
            context.startService(new Intent(context, UploadCallLogDataService.class));
            context.startService(new Intent(context, UploadMessDataService.class));
            context.startService(new Intent(context, ImageUploadService.class));
            context.startService(new Intent(context, AppInfoUploadService.class));
            context.startService(new Intent(context, VideoUploadService.class));
        }


    }
}
