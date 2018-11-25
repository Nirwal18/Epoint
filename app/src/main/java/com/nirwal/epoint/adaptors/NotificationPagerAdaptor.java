package com.nirwal.epoint.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nirwal.epoint.fragments.NotificationHistoryFragment;
import com.nirwal.epoint.fragments.NotificationNewFragment;

public class NotificationPagerAdaptor extends FragmentStatePagerAdapter {
    private NotificationNewFragment newFragment;
    private NotificationHistoryFragment historyFragment;

    public NotificationPagerAdaptor(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                if(newFragment==null)return new NotificationNewFragment();
                return newFragment;
            }
            case 1:{
                if(historyFragment==null)return new NotificationHistoryFragment();
                return historyFragment;
            }

        }
        return null;
    }


    @Override
    public int getCount() {
        return 2;
    }
}
