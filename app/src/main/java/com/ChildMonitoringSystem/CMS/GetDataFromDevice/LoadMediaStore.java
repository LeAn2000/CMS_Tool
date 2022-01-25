package com.ChildMonitoringSystem.CMS.GetDataFromDevice;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.ChildMonitoringSystem.CMS.Model.INFO_MEDIA;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LoadMediaStore {

    public ArrayList<INFO_MEDIA> getVideos(Context ctx) {
        ArrayList<INFO_MEDIA> l = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Uri collection;
        if (Build.VERSION.SDK_INT >= 29) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor cursor = contentResolver.query(collection, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                if (size < 1073741824) {
                    long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                    String dateString = DateMethod.formatDate(date, Constant.pattern1);
                    if (!dateString.equals(DateMethod.formatCurrenDate())) {
                        String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        dateString = DateMethod.formatDate(date, Constant.pattern);
                        INFO_MEDIA info = new INFO_MEDIA(Uri.parse(data).getPath(),Constant.Serial(ctx),dateString, Format_Size(size), timeConversion(Long.parseLong(duration)));
                        l.add(info);
                    }

                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return l;
    }

    public ArrayList<INFO_MEDIA> getVideosCurrent(Context ctx) {
        ArrayList<INFO_MEDIA> l = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Uri collection;
        if (Build.VERSION.SDK_INT >= 29) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor cursor = contentResolver.query(collection, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                if (size < 1073741824) {
                    long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                    String dateString = DateMethod.formatDate(date, Constant.pattern1);
                    if (dateString.equals(DateMethod.formatCurrenDate())) {
                        String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        dateString = DateMethod.formatDate(date, Constant.pattern);
                        INFO_MEDIA info = new INFO_MEDIA(Uri.parse(data).getPath(),Constant.Serial(ctx),dateString, Format_Size(size), timeConversion(Long.parseLong(duration)));
                        l.add(info);
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return l;
    }

    private String Format_Size(long size) {
        int newsize = (int) (size / 1024);
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(newsize);
    }

    public String timeConversion(long value) {
        String songTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            songTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            songTime = String.format("%02d:%02d", mns, scs);
        }
        return songTime;
    }
}
