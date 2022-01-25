package com.ChildMonitoringSystem.CMS.ViewPAgerAdapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ChildMonitoringSystem.CMS.OnboradingFragment.OnBoard1Fragment;
import com.ChildMonitoringSystem.CMS.OnboradingFragment.OnBoard2Fragment;


public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new OnBoard1Fragment();
            case 1:
                return new OnBoard2Fragment();
            default:
                return new OnBoard1Fragment();
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}
