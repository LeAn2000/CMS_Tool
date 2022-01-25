package com.ChildMonitoringSystem.CMS;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ChildMonitoringSystem.CMS.ViewPAgerAdapter.ViewPagerAutoRunAdapter;
import com.ChildMonitoringSystem.CMS.ViewPAgerAdapter.ViewPagerNotiFiAdapter;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import me.relex.circleindicator.CircleIndicator3;

public class LastStepActivity extends AppCompatActivity {
    private LinearLayout linearLayout_notifi, linearLayout_autorun;
    private Button btn_kc;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_step);
        linearLayout_notifi = findViewById(R.id.liner1);
        linearLayout_autorun = findViewById(R.id.liner2);
        btn_kc = findViewById(R.id.btn_khoichay);
        linearLayout_notifi.setOnClickListener(v -> {
            OpenInforDialog(Gravity.CENTER);
        });
        linearLayout_autorun.setOnClickListener(v -> {
            OpenInforDialog_AutoRun(Gravity.CENTER);
        });
        btn_kc.setOnClickListener(v -> {
            OpenInforDialog_Exit(Gravity.CENTER);
        });
    }

    private void OpenInforDialog(int center) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_notification);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = center;
        window.setAttributes(windowLayoutParams);

        if (Gravity.CENTER == center) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        ViewPager2 view = dialog.findViewById(R.id.view_pager_notifi);
        CircleIndicator3 circleIndicator = dialog.findViewById(R.id.circle_notifi);
        Button btn = dialog.findViewById(R.id.btn_ok);
        ViewPagerNotiFiAdapter adapter = new ViewPagerNotiFiAdapter(this);
        view.setAdapter(adapter);
        circleIndicator.setViewPager(view);
        btn.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void OpenInforDialog_AutoRun(int center) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_autorun);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = center;
        window.setAttributes(windowLayoutParams);

        if (Gravity.CENTER == center) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        ViewPager2 view = dialog.findViewById(R.id.view_pager_autorun);
        CircleIndicator3 circleIndicator = dialog.findViewById(R.id.circle_autorun);
        Button btn = dialog.findViewById(R.id.btn_ok_autorun);
        ViewPagerAutoRunAdapter adapter = new ViewPagerAutoRunAdapter(this);
        view.setAdapter(adapter);
        circleIndicator.setViewPager(view);
        btn.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void OpenInforDialog_Exit(int center) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_exit);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = center;
        window.setAttributes(windowLayoutParams);

        if (Gravity.CENTER == center) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        Boolean check = AutoStartPermissionHelper.Companion.getInstance().isAutoStartPermissionAvailable(getApplicationContext(),true);
        Button btn = dialog.findViewById(R.id.btn_exit);
        TextView textView = dialog.findViewById(R.id.mytext);
        if(check)
        {
            textView.setText("Bạn có thể thoát ứng dụng sau khi cấp quyền tự động khởi chạy");
        }
        else
        {
            textView.setText("Quyền tự động khởi chạy chỉ hỗ trợ những nhà sản xuất sau:\n\t1. Xiaomi\n\t2. Redmi\n\t3. Letv\n\t4. Honor\n\t5.Oppo\n\t6. Vivo\n\t7. Huawei\n\t8. Samsung\n\t9. Asus\n\t10. One Plus\n\nBạn có thể thoát ứng dụng.");
        }
        btn.setOnClickListener(v -> {
            dialog.dismiss();
            if(check){
                AutoStartPermissionHelper.Companion.getInstance().getAutoStartPermission(getApplicationContext(), true, true);
            }
        });
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null) {
            dialog.dismiss();
        } else {
            return;
        }
    }
}