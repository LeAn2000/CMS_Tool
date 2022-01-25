package com.ChildMonitoringSystem.CMS.GetDataFromDevice;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.ChildMonitoringSystem.CMS.Model.INFO_IMAGE;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;

import java.util.ArrayList;

public class LoadImageFromExternalStorage {

    //Get all images
    public ArrayList<INFO_IMAGE> GetAllIMage(Context ctx) {
        ArrayList<INFO_IMAGE> ListOfImage = new ArrayList<>();
        String[] projection = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_TAKEN
        };
        Uri collection;
        if (Build.VERSION.SDK_INT >= 29) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor cursor = ctx.getContentResolver().query(collection, projection, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                String dateString = DateMethod.formatDate(date, Constant.pattern1);
                if (!dateString.equals(DateMethod.formatCurrenDate())) {
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    dateString = DateMethod.formatDate(date, Constant.pattern);
                    INFO_IMAGE inf = new INFO_IMAGE(data, Constant.Serial(ctx), dateString, null);
                    ListOfImage.add(inf);
                }
                continue;
            }
            cursor.close();
        }
        return ListOfImage;
    }

    //Get image current day
    public ArrayList<INFO_IMAGE> GetIMageDayByDay(Context ctx) {
        ArrayList<INFO_IMAGE> ListOfImage = new ArrayList<>();
        String[] projection = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_TAKEN
        };
        Uri collection;
        if (Build.VERSION.SDK_INT >= 29) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor cursor = ctx.getContentResolver().query(collection, projection, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                String dateString = DateMethod.formatDate(date, Constant.pattern1);
                if (dateString.equals(DateMethod.formatCurrenDate())) {
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    dateString = DateMethod.formatDate(date, Constant.pattern);
                    INFO_IMAGE inf = new INFO_IMAGE(data, Constant.Serial(ctx), dateString, null);
                    ListOfImage.add(inf);
                }
                continue;
            }
            cursor.close();
        }
        return ListOfImage;
    }


}
