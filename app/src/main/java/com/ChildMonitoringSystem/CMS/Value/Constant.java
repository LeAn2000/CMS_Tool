package com.ChildMonitoringSystem.CMS.Value;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import java.io.File;

public class Constant {
    public static final String Main_Url = "http://117.2.159.103:8080";
    public static final String UploadFile_Url = "http://117.2.159.103:8081";

    //Upload Image
    public static final String KEY_Seri = "SERI_PHONE";
    public static final String KEY_Img = "IMAGES_NAME";
    public static final String DATE_IMAGE = "DATE_IMAGE";

    //Upload Media

    public static final String MEDIA_NAME="MEDIA_NAME";
    public static final String DATE_MEDIA="DATE_MEDIA";
    public static final String DURATION="DURATION";
    //Upload AppInfo
    public static final String APPNAME = "APPNAME";
    public static final String ICON = "icon";
    public static final String SERI_PHONE = "SERI_PHONE";
    public static final String pNAME = "pNAME";
    public static final String verNAME = "verNAME";
    public static final String verCODE = "verCODE";


    //url main for show image
    public static final String Image_Url = "http://117.2.159.103:8080/Images/";

    //Infomation Device

    public static final String Serial(Context ctx)
    {
        return  Settings.Secure.getString(ctx.getContentResolver(),Settings.Secure.ANDROID_ID);
    }
    public static final int ANDROID_VER = Build.VERSION.SDK_INT;
    public static final String MODEL = Build.MODEL;
    public static final String BRAND = Build.BRAND;
    public static final String DEVICE = Build.DEVICE;
    public static final String PRODUCT = Build.PRODUCT;

    // pattern
    public static final String pattern = "yyyy-MM-dd HH:mm:ss";
    public static final String patternfirebase = "dd-MM-yyyy HH:mm:ss";
    public static final String pattern1 = "dd-MM-yyyy";
    public static final String pattern2 = "yyyy-MM-dd HH-mm-ss";
    public static final String pattern3 = "dd/MM/yyyy";
    public static final String pattern_date_name = "dd-MM-yyy_hh-mm-ss";


    //
    public static final String ACTION = "com.cms.CMS";
    public static final String ACTION_1 = "com.cms.CMS1";
    //
    public static final String AUDIO_ACTION = "com.cms.Audio";
    public static final String LOCATION_ACTION = "com.cms.Location";
    public static final String ALL_SERVICE = "com.cms.ALL";
    //
    public static final File myDir = new File(Environment.getExternalStorageDirectory() + "/req_images");
    //
    public static final String OpenCode = "*6656#";

    public static final String KEY_FIRST = "KEYFIRST";
    public static final String KEY_PHONE = "KEY_PHONE";
    public static final String KEY_NAME = "KEY_NAME";





}
