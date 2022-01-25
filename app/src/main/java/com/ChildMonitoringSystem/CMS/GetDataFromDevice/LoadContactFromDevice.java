package com.ChildMonitoringSystem.CMS.GetDataFromDevice;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.ChildMonitoringSystem.CMS.Model.INFO_PHONEBOOK;
import com.ChildMonitoringSystem.CMS.Value.Constant;

import java.util.ArrayList;

public class LoadContactFromDevice {
    public ArrayList<INFO_PHONEBOOK> Load_Contact(Context ctx) {
        ArrayList<INFO_PHONEBOOK> list_of_phonebook = new ArrayList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        Cursor cursor = ctx.getContentResolver().query(uri,null,null,null,sort);
        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uri_num = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?";
                Cursor cursor_num = ctx.getContentResolver().query(uri_num,null,selection,new String[]{id},null);
                if(cursor_num.moveToNext())
                {
                    String number = cursor_num.getString(cursor_num.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    INFO_PHONEBOOK info = new INFO_PHONEBOOK(replacePhone(number),name, Constant.Serial(ctx));
                    list_of_phonebook.add(info);
                }
                cursor_num.close();
            }
        }
        cursor.close();
        return list_of_phonebook;
    }



    private String replacePhone(String phone)
    {
        String b1 = phone.replaceAll("\\s","");
        return b1.replace("+84","0");//replace +84 to 0
    }


}

