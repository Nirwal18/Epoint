package com.nirwal.epoint.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.epoint.R;

public class NotificationHistoryFragment extends Fragment {

    public NotificationHistoryFragment(){
    //deafault
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_notification_fragment,container,false);

        return v;
    }
}
