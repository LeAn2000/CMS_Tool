package com.ChildMonitoringSystem.CMS.BroadCast;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.util.List;

public class Helper {
    public static boolean Checknetwork(Context ctx) {
        if (ctx == null)
            return false;
        ConnectivityManager con = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con == null)
            return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network network = con.getActiveNetwork();
            if (network == null)
                return false;
            NetworkCapabilities networkCapabilities = con.getNetworkCapabilities(network);
            return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            NetworkInfo net = con.getActiveNetworkInfo();
            return net != null && net.isConnected();
        }
    }
}
