package com.ChildMonitoringSystem.CMS;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ChildMonitoringSystem.CMS.Model.ACCOUNT;
import com.ChildMonitoringSystem.CMS.Model.INFORMATION_PHONE;
import com.ChildMonitoringSystem.CMS.Model.MySharereference;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.OnBoard1Fragment;
import com.ChildMonitoringSystem.CMS.Service.Service;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;
import com.ChildMonitoringSystem.CMS.ui.CustomProgess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends CustomProgess {

    EditText edit_tk, edit_pass, edit_name;
    Button btn_login;
    String phone, password, name;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        edit_tk = findViewById(R.id.Edit_tk);
        edit_pass = findViewById(R.id.Edit_pass);
        edit_name = findViewById(R.id.Edit_Name);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edit_tk.getText().toString();
                password = edit_pass.getText().toString();
                name = edit_name.getText().toString();
                if(!checknull(phone)){
                    if(!checknull(password))
                    {
                        OpenDialog(Gravity.CENTER, dialog);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ACCOUNT a = new ACCOUNT(phone, password, null, null, null, false, null);
                                DangNhap(a);
                            }
                        },2000);

                    }
                    else
                    {
                        edit_pass.setError("Không được để trống");
                    }

                }
                else
                {
                    edit_tk.setError("Không được để trống");
                }

            }
        });
    }

    private boolean checknull(String c)
    {
        return c.length() == 0;
    }
    private boolean checkdialognull(Dialog dialog)
    {
        return dialog == null;
    }


    public void DangNhap(ACCOUNT account) {
        Service.apiservice.sendPost(account).enqueue(new Callback<ACCOUNT>() {
            @Override
            public void onResponse(Call<ACCOUNT> call, Response<ACCOUNT> response) {
                ACCOUNT acc = response.body();
                if (acc != null) {
                    CancleDialog(dialog);
                    MySharereference mySharereference = new MySharereference(getApplicationContext());
                    mySharereference.putValueString(Constant.KEY_PHONE,phone);
                    mySharereference.putValueString(Constant.KEY_NAME,name);
                    Intent intent = new Intent(getApplicationContext(), OnboardingActivity.class);
                    startActivity(intent);

                    finish();

                } else {

                    Toast.makeText(LoginActivity.this, "Mật khẩu hoặc số điện thoại của ban chưa đúng", Toast.LENGTH_SHORT).show();
                    CancleDialog(dialog);
                }


            }

            @Override
            public void onFailure(Call<ACCOUNT> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại" + t.getMessage(), Toast.LENGTH_SHORT).show();
                CancleDialog(dialog);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!checkdialognull(dialog))
            dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!checkdialognull(dialog))
            dialog.dismiss();
    }
}