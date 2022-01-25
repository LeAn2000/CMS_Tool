package com.ChildMonitoringSystem.CMS.ViewPAgerAdapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ChildMonitoringSystem.CMS.OnboradingFragment.AutoRunFragment1;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.AutoRunFragment2;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.AutoRunFragment3;


public class ViewPagerAutoRunAdapter extends FragmentStateAdapter {


    public ViewPagerAutoRunAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new AutoRunFragment1();
            case 1:
                return new AutoRunFragment2();
            case 2:
                return new AutoRunFragment3();
            default:
                return new AutoRunFragment1();
        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
