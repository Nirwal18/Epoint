package com.nirwal.epoint.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class BroadCastReciver extends BroadcastReceiver {
   private OnBroadCastListner _listner;

   public void setOnBroadCastListner(OnBroadCastListner listner){
       this._listner=listner;
   }


   @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if(_listner!=null) _listner.onEvent(EventType.Connectivity,!noConnectivity);
        }
    }




    public enum EventType{
        Connectivity
    }


    public interface OnBroadCastListner{
        void onEvent(EventType eventType,Object data);
    }
}
