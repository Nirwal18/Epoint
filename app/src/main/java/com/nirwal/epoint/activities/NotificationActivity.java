package com.nirwal.epoint.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nirwal.epoint.R;
import com.nirwal.epoint.adaptors.NotificationPagerAdaptor;

public class NotificationActivity extends AppCompatActivity {

    TabLayout _tabLayout;
    ViewPager _viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        _tabLayout = findViewById(R.id.notification_tabLayout);
        _viewPager = findViewById(R.id.notification_viewPager);

        _tabLayout.addTab(_tabLayout.newTab().setText("New"),true);
        _tabLayout.addTab(_tabLayout.newTab().setText("History"));
        _tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        _viewPager.setAdapter(new NotificationPagerAdaptor(this.getSupportFragmentManager()));
        //sets tab position on swipe
        _viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(_tabLayout));
        _tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //sets tab fragment on tab click
                _viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


//        getSupportFragmentManager()
//                .beginTransaction()
//                .show(getSupportFragmentManager().findFragmentById(R.id.notification_frag1))
//                .commit();

    }
}
