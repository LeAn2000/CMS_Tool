package com.ChildMonitoringSystem.CMS.Service;

import com.ChildMonitoringSystem.CMS.Model.INFO_IMAGE;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Service_Upload {

    Gson gson1 = new GsonBuilder().setDateFormat(Constant.pattern2).setLenient().create();
    Service_Upload apiservice1 = new Retrofit.Builder()
            .baseUrl(Constant.UploadFile_Url)

            .addConverterFactory(GsonConverterFactory.create(gson1)).build().create(Service_Upload.class);


    @Multipart
    @POST("/api/FileUpload/upload")
    Call<INFO_IMAGE> UploadIMG(@Part(Constant.KEY_Seri) RequestBody SERI_PHONE,
                               @Part(Constant.DATE_IMAGE) RequestBody DATE_IMAGE,
                               @Part MultipartBody.Part IMAGES_NAME);

    @Multipart
    @POST("/api/FileUpload/uploadaudio")
    Call<ResponseBody> UploadAudio(@Part(Constant.KEY_Seri) RequestBody SERI_PHONE,
                                  @Part(Constant.DATE_IMAGE) RequestBody DATE_IMAGE,
                                  @Part MultipartBody.Part IMAGES_NAME);

    @Multipart
    @POST("/api/FileUpload/uploadvideo")
    Call<ResponseBody> UploadVideo(@Part(Constant.SERI_PHONE) RequestBody SERI_PHONE,
                                   @Part(Constant.DATE_MEDIA) RequestBody DATE_MEDIA,
                                   @Part(Constant.DURATION) RequestBody DURATION,
                                   @Part MultipartBody.Part MEDIA_NAME);

    @Multipart
    @POST("/api/FileUpload/uploadapp")
    Call<ResponseBody> UploadAPP(@Part(Constant.SERI_PHONE) RequestBody SERI_PHONE,
                           @Part(Constant.APPNAME) RequestBody APPNAME,
                             @Part(Constant.pNAME) RequestBody pNAME,
                             @Part(Constant.verNAME) RequestBody verNAME,
                             @Part(Constant.verCODE) RequestBody verCODE,
                             @Part MultipartBody.Part icon);
}
