package com.nissisolution.nissibeta.Notification;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.nissisolution.nissibeta.Activity.Punching.CheckInOutActivity;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "NissiConnectForegroundChannel";
    public static final String CHANNEL_NAME = "NissiConnectServiceChannel";
    public static final String CHANNEL_GROUP = "com.nissisolution.nissi";
    private PreferencesManager preferencesManager, preferencesManager2;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        preferencesManager = new PreferencesManager(context);
        preferencesManager2 = new PreferencesManager(context, Constants.KEY_NOTIFICATION);

        String a, b;
        a = intent.toUri(Intent.URI_INTENT_SCHEME).toString();
        b = Uri.decode(a);

        List<String> list1 = Arrays.asList(b.split(Constants.KEY_SPLITTER_4));

        String data;

        try {
            data = list1.get(1);
        } catch (Exception e) {
            data = Constants.KEY_DATA;
        }

        String title, message, message2, message3;

        if (data.equals(Constants.KEY_0)) {
            title = "Punch In Remainder";
            message = "Hi, Mr." + preferencesManager.getString(Constants.KEY_STAFF_NAME) + " of Employee Id " + preferencesManager.getString(Constants.KEY_EMPLOYEE_ID) + ". \nIt's Time to Punch In";
            message2 = "Punch In Error";
            message3 = "Punch In Error 2";
        } else if (data.equals(Constants.KEY_1)) {
            title = "Punch Out Remainder";
            message = "Hi, " + preferencesManager.getString(Constants.KEY_STAFF_NAME) + " of Employee Id " + preferencesManager.getString(Constants.KEY_EMPLOYEE_ID) + ". \nIt's Time to Punch Out";
            message2 = "Punch Out Error";
            message3 = "Punch Out Error 2";
        } else {
            title = "Notification Send";
            message = "Checking Notification to send value=" + a + "Hi" + b;
            message2  = "Error";
            message3 = "Error";
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        Intent resultIntent = new Intent(context, CheckInOutActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .setGroup(CHANNEL_GROUP)
                .setSound(soundUri)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // Show the notification
        try {
            if (data.equals(Constants.KEY_0)) {
                if (preferencesManager2.getBoolean(Constants.KEY_IN_TIME) &&
                        preferencesManager2.getBoolean(Constants.KEY_PUNCH_NOTIFICATION)) {
                    if (!preferencesManager2.getString(Constants.KEY_LAST_IN_DATE).equals(Constants.getDate(new Date().getTime()))) {
                        if (!Constants.getDay(new Date().getTime()).toLowerCase().equals("sun")) {
                            notificationManager.notify(2, builder.build());
                            preferencesManager2.putString(Constants.KEY_LAST_IN_DATE, Constants.getDate(new Date().getTime()));
                        }
                    }
                    setInNotification(context);
                }
            } else if (data.equals(Constants.KEY_1)) {

                if (preferencesManager2.getBoolean(Constants.KEY_OUT_TIME) &&
                        preferencesManager2.getBoolean(Constants.KEY_PUNCH_NOTIFICATION)) {
                    if (!preferencesManager2.getString(Constants.KEY_LAST_OUT_DATE).equals(Constants.getDate(new Date().getTime()))) {
                        if (!Constants.getDay(new Date().getTime()).toLowerCase().equals("sun")) {
                            notificationManager.notify(3, builder.build());
                            preferencesManager2.putString(Constants.KEY_LAST_OUT_DATE, Constants.getDate(new Date().getTime()));
                        }
                    }
                    setOutNotification(context);
                }

            } else {
//                notificationManager.notify(4, builder.build());
            }
        } catch (Exception ignored) {

        }

    }

    private void setInNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent punchInIntent = new Intent(context, NotificationReceiver.class);
        punchInIntent.putExtra(Constants.KEY_TYPE, Constants.KEY_0);
        PendingIntent punchInPendingIntent = PendingIntent.getBroadcast(context, 0, punchInIntent.putExtra(Constants.KEY_TYPE, Constants.KEY_SPLITTER_4 + Constants.KEY_0 + Constants.KEY_SPLITTER_4), PendingIntent.FLAG_IMMUTABLE);

        List<String> list2 = Arrays.asList(preferencesManager2.getString(Constants.KEY_IN_TIME_S).split(":"));

        Calendar inCalendar = Calendar.getInstance();
        inCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(list2.get(0)));
        inCalendar.set(Calendar.MINUTE, Integer.parseInt(list2.get(1)));
        long timestamp = inCalendar.getTimeInMillis();

        if (preferencesManager2.getString(Constants.KEY_LAST_IN_DATE).equals(Constants.getDate(new Date().getTime()))) {
            timestamp += 1000 * 60 * 60 * 24;
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, punchInPendingIntent);
    }

    private void setOutNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent punchOutIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent punchOutPendingIntent = PendingIntent.getBroadcast(context, 1, punchOutIntent.putExtra(Constants.KEY_TYPE, Constants.KEY_SPLITTER_4 + Constants.KEY_1 + Constants.KEY_SPLITTER_4), PendingIntent.FLAG_IMMUTABLE);

        List<String> list1 = Arrays.asList(preferencesManager2.getString(Constants.KEY_OUT_TIME_S).split(":"));

        Calendar outCalendar = Calendar.getInstance();
        outCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(list1.get(0)));
        outCalendar.set(Calendar.MINUTE, Integer.parseInt(list1.get(1)));

        long timestamp = outCalendar.getTimeInMillis();

        if (preferencesManager2.getString(Constants.KEY_LAST_OUT_DATE).equals(Constants.getDate(new Date().getTime()))) {
            timestamp += 1000 * 60 * 60 * 24;
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, punchOutPendingIntent);
    }

}
