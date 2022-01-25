package com.ChildMonitoringSystem.CMS;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ChildMonitoringSystem.CMS.ViewPAgerAdapter.ViewPagerAdapter;

import me.relex.circleindicator.CircleIndicator3;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewpager;
    private CircleIndicator3 circleIndicator;
    private LinearLayout linearLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private TextView txt;
    private long Backpresstime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        InitUI();
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewpager.setAdapter(viewPagerAdapter);
        circleIndicator.setViewPager(viewpager);
        viewpager.setUserInputEnabled(false);
    }
    private void InitUI()
    {
        viewpager = findViewById(R.id.view_pager);
        circleIndicator = findViewById(R.id.circle);
        linearLayout = findViewById(R.id.layout_next);
        txt = findViewById(R.id.step);
        linearLayout.setOnClickListener(v -> {
            if(viewpager.getCurrentItem()==0)
            {
                viewpager.setCurrentItem(1);
            }
            if(viewpager.getCurrentItem()==1)
            {
                txt.setOnClickListener(v1 -> {
                    Intent intent = new Intent(getApplicationContext(),CallServiceActivity.class);
                    startActivity(intent);
                });
            }

        });
    }

    @Override
    public void onBackPressed() {
        if(Backpresstime+2000>System.currentTimeMillis())
        {
            super.onBackPressed();
        }
        else
            Toast.makeText(this, "Chạm lại để thoát ứng dụng", Toast.LENGTH_SHORT).show();
        Backpresstime = System.currentTimeMillis();

    }
}