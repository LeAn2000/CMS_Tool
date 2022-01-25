package com.ChildMonitoringSystem.CMS;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadImageFromExternalStorage;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadInfoAppFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadMessageFromDevice;
import com.ChildMonitoringSystem.CMS.Model.INFO_IMAGE;
import com.ChildMonitoringSystem.CMS.Model.INFO_MEDIA;
import com.ChildMonitoringSystem.CMS.Model.INFO_MESSAGE;
import com.ChildMonitoringSystem.CMS.Service.Service;
import com.ChildMonitoringSystem.CMS.Service.Service_Upload;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAin";
    ArrayList<INFO_MESSAGE> info_messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



    }



    private String Format_Size(long size)
    {
        int newsize  = (int) (size/1024);
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(newsize)+" KB";
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

    private void UPload(ArrayList<INFO_MEDIA>lst)
    {
        for (INFO_MEDIA info : lst) {
            String seri = info.getSERI_PHONE();
            String date = info.getDATE_MEDIA();
            String duaration  = info.getDURATION();
            RequestBody requestBodySeri = RequestBody.create(MediaType.parse("multipart/form-data"), seri);
            RequestBody requestBodyDate = RequestBody.create(MediaType.parse("multipart/form-data"), date);
            RequestBody requestBodyDuration = RequestBody.create(MediaType.parse("multipart/form-data"), duaration);
            File file = new File(info.getMEDIA_NAME());
            RequestBody requestBodyimg = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part mPart = MultipartBody.Part.createFormData(Constant.MEDIA_NAME, file.getName(), requestBodyimg);
            Service_Upload.apiservice1.UploadVideo(requestBodySeri, requestBodyDate,requestBodyDuration, mPart).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("Call request", call.request().toString());
                    Log.d("Call request header", call.request().headers().toString());
                    Log.d("Response raw header", response.headers().toString());
                    Log.d("Response raw", String.valueOf(response.raw().body()));
                    Log.d("Response code", String.valueOf(response.code()));

                    if(response.isSuccessful()) {
                        //the response-body is already parseable to your ResponseBody object
                        ResponseBody responseBody = (ResponseBody) response.body();
                        //you can do whatever with the response body now...
                        String responseBodyString= null;
                        try {
                            responseBodyString = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response body", responseBodyString);
                    }
                    else  {
                        Log.d("Response errorBody", String.valueOf(response.errorBody()));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    String err = t.getMessage() == null ? "" : t.getMessage();
                    Log.i("TAG", "onFailure: "+err);
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}




