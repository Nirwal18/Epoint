package com.nirwal.epoint;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nirwal.epoint.database.DatabaseHelper;
import com.nirwal.epoint.services.BroadCastReciver;

public class MyApp extends Application {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseHelper sqlDb;
    private FirebaseAnalytics _firebaseAnalytics;

    private FirebaseAuth _firebaseAuth;


    public static final String App_Name = "Epoint";
    public static final String CHANNEL_1_ID = "general_notification";
    public static final String CHANNEL_2_ID = "Channel2";

    private BroadCastReciver _broadCastReciver;


    @Override
    public void onCreate() {
        super.onCreate();

        firebaseDatabase = FirebaseDatabase.getInstance();
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        _firebaseAuth = FirebaseAuth.getInstance();


        sqlDb = new DatabaseHelper(this);
        createNotificationChannels();

        _broadCastReciver = new BroadCastReciver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(_broadCastReciver,intentFilter);

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(_broadCastReciver);
    }

    public BroadCastReciver getBroadCastReciver(){
            return _broadCastReciver;
    }





    public FirebaseDatabase getFirebaseDatabase() { return firebaseDatabase; }

    public DatabaseHelper getSqlDb() { return sqlDb;  }

    private void createNotificationChannels(){

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Epoint general notification",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel1.setDescription("Alarm Low");

            NotificationChannel notificationChannel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel2.setDescription("Alarm High");


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
            notificationManager.createNotificationChannel(notificationChannel2);

        }


    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return _firebaseAnalytics;
    }

    public FirebaseAuth getFirebaseAuth() {
        return _firebaseAuth;
    }
}
