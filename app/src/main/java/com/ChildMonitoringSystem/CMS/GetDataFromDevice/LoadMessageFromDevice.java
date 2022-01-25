package com.ChildMonitoringSystem.CMS.GetDataFromDevice;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Model.INFO_MESSAGE;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;

import java.util.ArrayList;

public class LoadMessageFromDevice {

    //Get all SMS
    public ArrayList<INFO_MESSAGE> Load_SMS(Context ctx) {
        ArrayList<INFO_MESSAGE> List_Of_Message = new ArrayList<>();
        Cursor cursor = ctx.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null,
                null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                long date = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE));
                String datetime = DateMethod.formatDate(date, Constant.pattern1);

                if (!datetime.equals(DateMethod.formatCurrenDate())) {
                    String name = "";
                    String phonenum = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                    String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                    int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));
                    datetime = DateMethod.formatDate(date, Constant.pattern);
                    INFO_MESSAGE mess = new INFO_MESSAGE(replacePhone(phonenum), name, FomattType(type), body, datetime, Constant.Serial(ctx));
                    List_Of_Message.add(mess);
                }
            }
            cursor.close();
        }
        return List_Of_Message;
    }

    //Get sms current day
    public ArrayList<INFO_MESSAGE> Load_SMS_DbyD(Context ctx) {
        ArrayList<INFO_MESSAGE> List_Of_Message = new ArrayList<>();
        Cursor cursor = ctx.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null,
                null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            while (cursor.moveToPrevious()) {
                long date = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE));
                String datetime = DateMethod.formatDate(date, Constant.pattern1);
                if (datetime.equals(DateMethod.formatCurrenDate())) {
                    String name = "";
                    String phonenum = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                    String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                    int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));
                    datetime = DateMethod.formatDate(date, Constant.pattern);
                    INFO_MESSAGE mess = new INFO_MESSAGE(replacePhone(phonenum), name, FomattType(type), body, datetime, Constant.Serial(ctx));
                    List_Of_Message.add(mess);
                }
            }
            cursor.close();
        }
        return List_Of_Message;
    }

    private String FomattType(int type) {
        if (type == 1)
            return "Tin nhắn đến";
        else if (type == 2)
            return "Tin nhắn gửi đi";
        else if (type == 3)
            return "Tin nháp";
        else if (type == 4)
            return "Outbox";
        else if (type == 5)
            return "Gửi thất bại";
        else if (type == 6)
            return "Hàng chờ gửi sau";
        else
            return "Không xác định";

    }

    private String replacePhone(String phone) {
        return phone.replace("+84", "0");//replace +84 to 0
    }

}
