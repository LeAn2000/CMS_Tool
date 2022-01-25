package com.ChildMonitoringSystem.CMS.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharereference {
    private static final String MY_SHARE  = "MY_SHARE";
    private Context mContext;

    public MySharereference(Context mContext) {
        this.mContext = mContext;
    }

    public void putValue(String key, boolean value)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean getValue(String key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE,0);
        return sharedPreferences.getBoolean(key,false);
    }
    public void putValueString(String key, String value)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getValueString(String key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE,0);
        return sharedPreferences.getString(key,"");
    }
}
