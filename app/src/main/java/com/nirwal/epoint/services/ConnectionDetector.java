package com.nirwal.epoint.services;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
         if(connectivityManager== null){
             return false;
         }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
         if(info==null){
             return false;
         }
         if(info.getState()!= NetworkInfo.State.CONNECTED) {
             return false;
         }
        return true;
    }
}
