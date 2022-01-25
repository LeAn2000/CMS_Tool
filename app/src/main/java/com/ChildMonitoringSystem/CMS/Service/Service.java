package com.ChildMonitoringSystem.CMS.Service;

import com.ChildMonitoringSystem.CMS.Model.ACCOUNT;
import com.ChildMonitoringSystem.CMS.Model.INFORMATION_PHONE;
import com.ChildMonitoringSystem.CMS.Model.INFO_CALL_LOG;
import com.ChildMonitoringSystem.CMS.Model.INFO_MESSAGE;
import com.ChildMonitoringSystem.CMS.Model.INFO_PHONEBOOK;
import com.ChildMonitoringSystem.CMS.Model.LocationLocalTrack;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Service {
    //Khởi tạo
    Gson gson = new GsonBuilder().setDateFormat(Constant.pattern2).create();
    Service apiservice = new Retrofit.Builder()
            .baseUrl(Constant.Main_Url)
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(Service.class);

    // Đăng nhập
    @POST("api/Login")
    Call<ACCOUNT> sendPost(@Body ACCOUNT acc);
    // post danh bạ
    @POST("/api/postphonebySeri")
    Call<ArrayList<INFO_PHONEBOOK>> sendPhoneBook(@Body ArrayList<INFO_PHONEBOOK> phonebook);
    //post lịch sử cuộc gọi
    @POST("/api/postcallbySeri")
    Call<ArrayList<INFO_CALL_LOG>> sendCallLog(@Body ArrayList<INFO_CALL_LOG> callLog);
    //post tin nhắn
    @POST("/api/postmessagebySeri")
    Call<ArrayList<INFO_MESSAGE>> sendMessage(@Body ArrayList<INFO_MESSAGE> message);
    //post thông tin điện thoại
    @POST("/api/postinfophonebyPhoneUser")
    Call<INFORMATION_PHONE> sendInforPhone(@Body INFORMATION_PHONE information_phone);
    // post vị tri
    @POST("/api/postLocation")
    Call<List<LocationLocalTrack>> sendCoorOff(@Body List<LocationLocalTrack> localTracks);


}
