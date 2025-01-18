package com.example.d308.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.d308.R;

public class ExcursionAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String excursionTitle = intent.getStringExtra("EXCURSION_TITLE");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "excursion_notifications")
                .setSmallIcon(R.drawable.ic_notification) // Replace with your icon
                .setContentTitle("Excursion Alert")
                .setContentText("Don't forget your excursion: " + excursionTitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}