package com.oss_net.choloeksathe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oss_net.choloeksathe.fragments.driver.DriverCarProfileFragment;
import com.oss_net.choloeksathe.fragments.driver.DriverProfileFragment;

/**
 * Created by mahbubhasan on 12/16/17.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DriverProfileFragment();
            case 1:
                return new DriverCarProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
