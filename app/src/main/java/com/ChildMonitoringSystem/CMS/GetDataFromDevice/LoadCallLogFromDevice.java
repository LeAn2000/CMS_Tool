package com.ChildMonitoringSystem.CMS.GetDataFromDevice;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.ChildMonitoringSystem.CMS.Model.INFO_CALL_LOG;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;

import java.util.ArrayList;

public class LoadCallLogFromDevice {
    public ArrayList<INFO_CALL_LOG> LoadAllCallLog(Context ctx) {
        ArrayList<INFO_CALL_LOG> ListOfCall = new ArrayList<>();
        Cursor cursor = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            long Date1 = cursor.getLong( cursor.getColumnIndex(CallLog.Calls.DATE));
            String Datetime = DateMethod.formatDate(Date1, Constant.pattern1);
            if (!Datetime.equals(DateMethod.formatCurrenDate())) {
                String Numberphone = replacePhone(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                String Name = formatname(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                String Duration = DurationFormat(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                String Type = TypeofCalllog(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))));
                Datetime = DateMethod.formatDate(Date1, Constant.pattern);
                INFO_CALL_LOG call_log = new INFO_CALL_LOG(replacePhone(Numberphone), Name, Type, Datetime, Duration, Constant.Serial(ctx));
                ListOfCall.add(call_log);
            }
        }
        cursor.close();
        return ListOfCall;
    }

    public ArrayList<INFO_CALL_LOG> LoadAllCallLogCurrentDay(Context ctx) {
        ArrayList<INFO_CALL_LOG> ListOfCall = new ArrayList<>();
        Cursor cursor = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            long Date1 = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            String Datetime = DateMethod.formatDate(Date1, Constant.pattern1);
            if (Datetime.equals(DateMethod.formatCurrenDate())) {
                String Numberphone = replacePhone(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                String Name = formatname(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                String Duration = DurationFormat(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                String Type = TypeofCalllog(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))));
                Datetime = DateMethod.formatDate(Date1, Constant.pattern);
                INFO_CALL_LOG call_log = new INFO_CALL_LOG(replacePhone(Numberphone), Name, Type, Datetime, Duration, Constant.Serial(ctx));
                ListOfCall.add(call_log);
            }
        }
        cursor.close();
        return ListOfCall;
    }


    private String formatname(String name) {
        if (name != null)
            return name;
        return "Không xác định";
    }

    private String TypeofCalllog(int type) {

        if (type == 1) return "Cuộc gọi đến";
        else if (type == 2) return "Cuộc gọi đi";
        else if (type == 3) return "Cuộc gọi nhỡ";
        else if (type == 5) return "Từ chối";
        return "Không xác định";
    }

    private String DurationFormat(String duration) {
        String durationFormatted = null;
        if (Integer.parseInt(duration) < 60) {
            durationFormatted = duration + " giây";
        } else {
            int min = Integer.parseInt(duration) / 60;
            int sec = Integer.parseInt(duration) % 60;
            if (sec == 0)
                durationFormatted = min + " phút";
            else
                durationFormatted = min + " phút " + sec + " giây";
        }
        return durationFormatted;
    }

    private String replacePhone(String phone)
    {
        return phone.replace("+84","0");//replace +84 to 0
    }
}
