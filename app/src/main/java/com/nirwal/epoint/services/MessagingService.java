package com.nirwal.epoint.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.activities.NotificationActivity;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        sendNotification(remoteMessage.getNotification().getBody());
    }


    void sendNotification(String msg){

        PendingIntent pendingIntent = PendingIntent
                .getActivity(this,0,
                        new Intent(this,NotificationActivity.class),
                        PendingIntent.FLAG_ONE_SHOT);


        Notification  notification = new NotificationCompat.Builder(this)
                .setChannelId(((MyApp)this.getApplication()).CHANNEL_1_ID)
                .setContentTitle("New")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_appicon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
    }


}
