package com.example.madproject.ui.profile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationUtils {

    public static final String CHANNEL_ID = "general_notification_channel";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Define the importance level
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // Create the NotificationChannel
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "General Notification",
                    importance
            );
            channel.setDescription("This channel is used for general notifications.");

            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
