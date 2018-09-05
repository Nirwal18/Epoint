package com.nirwal.epoint;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.database.FirebaseDatabase;
import com.nirwal.epoint.database.DatabaseHelper;

public class MyApp extends Application {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseHelper sqlDb;
    public static final String App_Name = "Epoint";
    public static final String CHANNEL_1_ID = "Channel1";
    public static final String CHANNEL_2_ID = "Channel2";



    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDatabase = FirebaseDatabase.getInstance();
        sqlDb = new DatabaseHelper(this);
        createNotificationChannels();
    }

    public FirebaseDatabase getFirebaseDatabase() { return firebaseDatabase; }

    public DatabaseHelper getSqlDb() { return sqlDb;  }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel notificationChannel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel1.setDescription("This is Channel 1.");

            NotificationChannel notificationChannel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel2.setDescription("This is Channel 2.");


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
            notificationManager.createNotificationChannel(notificationChannel2);

        }


    }



}
