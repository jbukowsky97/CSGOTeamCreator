package com.hardboiled.csgoteamcreator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by wesley on 12/8/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification(context, "Hey, we've missed you!");
    }

    public void Notification(Context context, String message) {
        // Set Notification Title
        String strTitle = "CS:GO Team Builder";
        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, LoginActivity.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strTitle);
        intent.putExtra("text", message);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                // Set Icon
                .setSmallIcon(R.drawable.team_creator_logo)
                // Set Ticker Message
                .setTicker(message)
                // Set Title
                .setContentTitle("CS:GO Team Builder")
                // Set Text
                .setContentText(message)
                // Add an Action Button below Notification
                .addAction(R.drawable.team_creator_logo, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }
}
