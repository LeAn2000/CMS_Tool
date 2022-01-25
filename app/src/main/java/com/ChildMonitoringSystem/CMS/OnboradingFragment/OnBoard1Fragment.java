package com.ChildMonitoringSystem.CMS.OnboradingFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadCallLogFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadContactFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadMessageFromDevice;
import com.ChildMonitoringSystem.CMS.LoginActivity;
import com.ChildMonitoringSystem.CMS.Model.INFORMATION_PHONE;
import com.ChildMonitoringSystem.CMS.Model.MySharereference;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Service.Service;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoard1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoard1Fragment extends Fragment {

    MySharereference my;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OnBoard1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnBoard1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnBoard1Fragment newInstance(String param1, String param2) {
        OnBoard1Fragment fragment = new OnBoard1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_on_board1, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        my = new MySharereference(getContext());
        CheckPermission();
    }

    private void CheckPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                PostInForPhone(my.getValueString(Constant.KEY_PHONE),my.getValueString(Constant.KEY_NAME),"GRANTED",getContext());
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                if(!deniedPermissions.isEmpty())
                {

                    PostInForPhone(my.getValueString(Constant.KEY_PHONE),my.getValueString(Constant.KEY_NAME),deniedPermissions.toString(),getContext());
                }

            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .check();
    }
    public void PostInForPhone(String Phone_User, String name, String per, Context ctx) {
        INFORMATION_PHONE info = new INFORMATION_PHONE(Constant.Serial(ctx), Phone_User, per, Constant.MODEL, Constant.BRAND, Constant.DEVICE, Constant.PRODUCT, name, DateMethod.formatCurrenDateyyyy());
        Service.apiservice.sendInforPhone(info).enqueue(new Callback<INFORMATION_PHONE>() {
            @Override
            public void onResponse(Call<INFORMATION_PHONE> call, Response<INFORMATION_PHONE> response) {
                Log.i("TAG", "onResponse: send success ");
            }

            @Override
            public void onFailure(Call<INFORMATION_PHONE> call, Throwable t) {
                Log.i("TAG", "onResponse: send fail ");

            }
        });
    }

}