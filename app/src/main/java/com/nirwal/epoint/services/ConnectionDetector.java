package com.nirwal.epoint.services;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private Context _context;
    public ConnectionDetector(Context context){
        this._context=context;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Service.CONNECTIVITY_SERVICE);
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

         connectivityManager=null;
         info=null;
        return true;
    }
}
