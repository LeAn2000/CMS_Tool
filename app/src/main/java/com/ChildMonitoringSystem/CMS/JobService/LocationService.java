package com.ChildMonitoringSystem.CMS.JobService;

import static com.ChildMonitoringSystem.CMS.MyApplication.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.ChildMonitoringSystem.CMS.BroadCast.Helper;
import com.ChildMonitoringSystem.CMS.BroadCast.Restarter_Service;
import com.ChildMonitoringSystem.CMS.Model.INFO_PHONEBOOK;
import com.ChildMonitoringSystem.CMS.Model.LocationLocalTrack;
import com.ChildMonitoringSystem.CMS.Model.LocationTracker;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;
import com.ChildMonitoringSystem.CMS.database.Loc_DB_func;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {
    private DatabaseReference databaseReference;
    private  LocationCallback locationCallback ;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG", "onCreate: ");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.Serial(getApplicationContext()));

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
        Log.i("TAG", "onStartCommand: ");
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                String latin = Double.toString(locationResult.getLastLocation().getLatitude());
                String longtin = Double.toString(locationResult.getLastLocation().getLongitude());
                Context context = getApplicationContext();

                if(!Helper.Checknetwork(getApplicationContext()))
                {
                    Log.d("TAG", "onLocationResult: inssert");
                    Loc_DB_func.insertLoc(latin,longtin, context);
                }
                else{
                    Log.d("TAG", "onLocationResult: getlist");
                    List<LocationLocalTrack> locallist  = Loc_DB_func.getListLocal(context);
                    Log.d("TAG", "onLocationResult: inssert"+locallist.size());
                    SendCoorOff(locallist,context);
                    LocationTracker locationTracker = new LocationTracker(Constant.Serial(getApplicationContext()), latin, longtin, DateMethod.formatCurrenDateFirbase());
                    databaseReference.setValue(locationTracker);
                }

            }
        };
        Locationrequest();
        return START_STICKY;
    }

    private void SendCoorOff(List<LocationLocalTrack> localTracks,Context ctx) {
        if (localTracks.size() > 0) {
            com.ChildMonitoringSystem.CMS.Service.Service.apiservice.sendCoorOff(localTracks).enqueue(new Callback<List<LocationLocalTrack>>() {
                @Override
                public void onResponse(Call<List<LocationLocalTrack>> call, Response<List<LocationLocalTrack>> response) {
                    Log.d("TAG", "onResponse: delete");
                    Loc_DB_func.deleteAll(ctx);
                }

                @Override
                public void onFailure(Call<List<LocationLocalTrack>> call, Throwable t) {

                }
            });


        }
    }

    private void Locationrequest() {
        Log.i("TAG", "Locationrequest: ");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
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

        throw new UnsupportedOperationException("Not yet implemented");
    }



}
