package com.ChildMonitoringSystem.CMS.ViewPAgerAdapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ChildMonitoringSystem.CMS.OnboradingFragment.NotifiFragment1;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.NotifiFragment2;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.NotifiFragment3;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.NotifiFragment4;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.NotifiFragment5;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.NotifiFragment6;


public class ViewPagerNotiFiAdapter extends FragmentStateAdapter {


    public ViewPagerNotiFiAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new NotifiFragment1();
            case 1:
                return new NotifiFragment2();
            case 2:
                return new NotifiFragment3();
            case 3:
                return new NotifiFragment4();
            case 4:
                return new NotifiFragment5();
            case 5:
                return new NotifiFragment6();
            default:
                return new NotifiFragment1();
        }
    }
    @Override
    public int getItemCount() {
        return 6;
    }
}
