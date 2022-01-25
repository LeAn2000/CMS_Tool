package com.ChildMonitoringSystem.CMS.OnboradingFragment;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadCallLogFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadContactFromDevice;
import com.ChildMonitoringSystem.CMS.GetDataFromDevice.LoadMessageFromDevice;
import com.ChildMonitoringSystem.CMS.JobService.UploadDataService;
import com.ChildMonitoringSystem.CMS.Model.INFO_CALL_LOG;
import com.ChildMonitoringSystem.CMS.Model.INFO_MESSAGE;
import com.ChildMonitoringSystem.CMS.Model.INFO_PHONEBOOK;
import com.ChildMonitoringSystem.CMS.R;
import com.ChildMonitoringSystem.CMS.Service.Service;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoard2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoard2Fragment extends Fragment {

    private View mView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OnBoard2Fragment() {
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
    public static OnBoard2Fragment newInstance(String param1, String param2) {
        OnBoard2Fragment fragment = new OnBoard2Fragment();
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
        mView = inflater.inflate(R.layout.fragment_on_board2, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }



}