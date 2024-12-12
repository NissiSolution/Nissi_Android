package com.nissisolution.nissibeta.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.nissisolution.nissibeta.Activity.Punching.CheckInOutActivity;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;

public class NotificationReceiver2 extends BroadcastReceiver {
    private static final String CHANNEL_ID = "notification_channel_2";
    private PreferencesManager preferencesManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        preferencesManager = new PreferencesManager(context);

        // Notification channel (required for Android O and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Checking Alert",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Create an intent to open the app when the notification is clicked
        Intent resultIntent = new Intent(context, CheckInOutActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon) // Replace with your app's icon
                .setContentTitle("Small Check")
                .setContentText("Hi, " + preferencesManager.getString(Constants.KEY_STAFF_NAME) + " of Employee Id " + preferencesManager.getString(Constants.KEY_EMPLOYEE_ID) + ". It's Time to Punch Out")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        // Show the notification
            notificationManager.notify(1, builder.build());
    }
}
