package com.nissisolution.nissibeta.Notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Activity.CompOffHoliday.CompOffHolidayActivity;
import com.nissisolution.nissibeta.Activity.Leave.LeaveActivity;
import com.nissisolution.nissibeta.Activity.MissedPunch.MissedPunchActivity;
import com.nissisolution.nissibeta.Activity.NightShift.NightShiftActivity;
import com.nissisolution.nissibeta.Activity.Others.MainPageActivity;
import com.nissisolution.nissibeta.Classes.NotificationClasses;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ForegroundService extends Service {

    public static final String CHANNEL_ID = "NissiConnectForegroundChannel";
    public static final String CHANNEL_NAME = "NissiConnectServiceChannel";
    public static final String CHANNEL_GROUP = "com.nissisolution.nissi";
    private Context context;
    private PreferencesManager preferencesManager, preferencesManager2;
    private AlarmManager alarmManager;
    private List<NotificationClasses> notificationClassesList, punchList, punchRequestList, leaveList, leaveRequestList, compOffList, compOffRequestList, localHolidayList, localHolidayRequestList, nightShiftList, nightShiftRequestList;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra(Constants.KEY_DATA);

        context = this;
        preferencesManager = new PreferencesManager(context);
        preferencesManager2 = new PreferencesManager(context, Constants.KEY_NOTIFICATION);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        notificationClassesList = new ArrayList<>();
        punchList = new ArrayList<>();
        punchRequestList = new ArrayList<>();
        leaveList = new ArrayList<>();
        leaveRequestList = new ArrayList<>();
        compOffList = new ArrayList<>();
        compOffRequestList = new ArrayList<>();
        localHolidayList = new ArrayList<>();
        localHolidayRequestList = new ArrayList<>();
        nightShiftList = new ArrayList<>();
        nightShiftRequestList = new ArrayList<>();

            if (Objects.equals(input, Constants.KEY_0)) {
                stopForeground(true);
            } else {
                Intent notificationIntent = new Intent(context, MainPageActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                Intent dismissIntent = new Intent(this, ForegroundService.class);
                dismissIntent.putExtra(Constants.KEY_DATA, Constants.KEY_0);
                PendingIntent pendingIntent1 = PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE);

                createNotificationChannel();

                Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("Nissi Connect is running")
                        .setSmallIcon(R.mipmap.logo)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)
                        .addAction(R.drawable.vector_alert, "Dismiss", pendingIntent1)
                        .build();

                startForeground(599, notification);

                if (preferencesManager2.getBoolean(Constants.KEY_PUNCH_NOTIFICATION) && preferencesManager2.getBoolean(Constants.KEY_IN_TIME) && preferencesManager2.getString(Constants.KEY_IN_TIME_S) != null) {
                    Intent punchInIntent = new Intent(context, NotificationReceiver.class);
                    punchInIntent.putExtra(Constants.KEY_TYPE, Constants.KEY_0);
                    PendingIntent punchInPendingIntent = PendingIntent.getBroadcast(context, 0, punchInIntent.putExtra(Constants.KEY_TYPE, Constants.KEY_SPLITTER_4 + Constants.KEY_0 + Constants.KEY_SPLITTER_4), PendingIntent.FLAG_IMMUTABLE);

                    List<String> list1 = Arrays.asList(preferencesManager2.getString(Constants.KEY_IN_TIME_S).split(":"));

                    Calendar inCalendar = Calendar.getInstance();
                    inCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(list1.get(0)));
                    inCalendar.set(Calendar.MINUTE, Integer.parseInt(list1.get(1)));
                    long timestamp = inCalendar.getTimeInMillis();

                    if (preferencesManager2.getString(Constants.KEY_LAST_IN_DATE).equals(Constants.getDate(new Date().getTime()))) {
                        timestamp += 1000 * 60 * 60 * 24;
                    }

                    alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, punchInPendingIntent);
                }

                if (preferencesManager2.getBoolean(Constants.KEY_PUNCH_NOTIFICATION) && preferencesManager2.getBoolean(Constants.KEY_OUT_TIME) && preferencesManager2.getString(Constants.KEY_OUT_TIME_S) != null) {
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

                checkForNotifications();

            }

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
//        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void checkForNotifications() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handleNotification,
                    error -> {
                        new Handler().postDelayed(this::checkForNotifications, 15 * 60 * 1000);
                    }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_NOTIFICATION);
                    map.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        } catch (Exception ignored) {

        }
    }

    private void handleNotification(String response) {
        List<String> list1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_1));
        notificationClassesList.clear();
        for (int i = 1; i < list1.size(); i++) {
            List<String> list2 = Arrays.asList(list1.get(i).split(Constants.KEY_SPLITTER_2));
            notificationClassesList.add(new NotificationClasses(list2.get(0), list2.get(1),
                    Integer.parseInt(list2.get(2)), Integer.parseInt(list2.get(4)), Long.parseLong(list2.get(3)),
                    Integer.parseInt(list2.get(6)), Integer.parseInt(list2.get(5))));
        }
        setNotification();
        new Handler().postDelayed(this::checkForNotifications, 15 * 60 * 1000);
    }

    private void setNotification() {
        punchList.clear();
        punchRequestList.clear();
        localHolidayList.clear();
        localHolidayRequestList.clear();
        leaveList.clear();
        leaveRequestList.clear();
        compOffList.clear();
        compOffRequestList.clear();
        nightShiftList.clear();
        nightShiftRequestList.clear();

        for (NotificationClasses notificationClasses : notificationClassesList) {
            if (notificationClasses.viewed == 0) {
                switch (notificationClasses.type) {
                    case 0:
                        leaveList.add(notificationClasses);
                        break;
                    case 100:
                        leaveRequestList.add(notificationClasses);
                        break;
                    case 1:
                        compOffList.add(notificationClasses);
                        break;
                    case 101:
                        compOffRequestList.add(notificationClasses);
                        break;
                    case 2:
                        localHolidayList.add(notificationClasses);
                        break;
                    case 102:
                        localHolidayRequestList.add(notificationClasses);
                        break;
                    case 3:
                        nightShiftList.add(notificationClasses);
                        break;
                    case 103:
                        nightShiftRequestList.add(notificationClasses);
                        break;
                    case 10:
                        punchList.add(notificationClasses);
                        break;
                    case 110:
                        punchRequestList.add(notificationClasses);
                        break;
                    default:
                        break;

                }
            }
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        if (!leaveList.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : leaveList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, LeaveActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(leaveList.size() + " " + leaveList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.leave))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = leaveList.size() + " " + leaveList.get(leaveList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_LEAVE) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_LEAVE).equals(previousValue)) {
                notificationManager.notify(600, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_LEAVE, previousValue);
        }

        if (!leaveRequestList.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : leaveRequestList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, LeaveActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(leaveRequestList.size() + " " + leaveRequestList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.leave))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = leaveRequestList.size() + " " + leaveRequestList.get(leaveRequestList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_LEAVE_REQUEST) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_LEAVE_REQUEST).equals(previousValue)) {
                notificationManager.notify(601, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_LEAVE_REQUEST, previousValue);
        }

        // comp-off
        if (!compOffList.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : nightShiftList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, CompOffHolidayActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(compOffList.size() + " " + compOffList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.comp_off))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = compOffList.size() + " " + compOffList.get(compOffList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_COMP_OFF) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_COMP_OFF).equals(previousValue)) {
                notificationManager.notify(602, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_COMP_OFF, previousValue);
        }

        if (!compOffRequestList.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : compOffRequestList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, CompOffHolidayActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(compOffRequestList.size() + " " + compOffRequestList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.comp_off))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = compOffRequestList.size() + " " + compOffRequestList.get(compOffRequestList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_COMP_OFF_REQUEST) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_COMP_OFF_REQUEST).equals(previousValue)) {
                notificationManager.notify(603, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_COMP_OFF_REQUEST, previousValue);
        }

        if (!localHolidayList.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : localHolidayList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, CompOffHolidayActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(localHolidayList.size() + " " + localHolidayList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.comp_off))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = localHolidayList.size() + " " + localHolidayList.get(localHolidayList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_HOLIDAY) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_HOLIDAY).equals(previousValue)) {
                notificationManager.notify(604, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_HOLIDAY, previousValue);
        }

        if (!localHolidayRequestList.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : localHolidayRequestList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, CompOffHolidayActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(localHolidayRequestList.size() + " " + localHolidayRequestList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.comp_off))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = localHolidayRequestList.size() + " " + localHolidayRequestList.get(localHolidayRequestList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_HOLIDAY_REQUEST) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_HOLIDAY_REQUEST).equals(previousValue)) {
                notificationManager.notify(605, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_HOLIDAY_REQUEST, previousValue);
        }

        if (!nightShiftList.isEmpty()) {

            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : nightShiftList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, NightShiftActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(nightShiftList.size() + " " + nightShiftList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message.toString())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.night_shift))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = nightShiftList.size() + " " + nightShiftList.get(nightShiftList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_NIGHT_SHIFT) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_NIGHT_SHIFT).equals(previousValue)) {
                notificationManager.notify(606, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_NIGHT_SHIFT, previousValue);
        }

        if (!nightShiftRequestList.isEmpty()) {

            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : nightShiftRequestList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, NightShiftActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(nightShiftRequestList.size() + " " + nightShiftRequestList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message.toString())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.night_shift))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = nightShiftRequestList.size() + " " + nightShiftRequestList.get(nightShiftRequestList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_NIGHT_SHIFT_REQUEST) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_NIGHT_SHIFT_REQUEST).equals(previousValue)) {
                notificationManager.notify(607, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_NIGHT_SHIFT_REQUEST, previousValue);
        }

        if (!punchList.isEmpty()) {

            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : punchList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, MissedPunchActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(punchList.size() + " " + punchList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.missed_punch))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = punchList.size() + " " + punchList.get(punchList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_PUNCH) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_PUNCH).equals(previousValue)) {
                notificationManager.notify(608, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_PUNCH, previousValue);
        }

        if (!punchRequestList.isEmpty()) {

            StringBuilder message = new StringBuilder();
            for (NotificationClasses classes : punchRequestList) {
                if (message.toString().isEmpty()) {
                    message.append(classes.notification);
                } else {
                    message.append("\n").append(classes.notification);
                }
            }

            Intent intent = new Intent(context, MissedPunchActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(punchRequestList.size() + " " + punchRequestList.get(0).title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.missed_punch))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(soundUri);

            String previousValue = punchRequestList.size() + " " + punchRequestList.get(punchRequestList.size() - 1).timestamp;

            if (preferencesManager.getString(Constants.KEY_PREVIOUS_PUNCH_REQUEST) == null ||
                    !preferencesManager.getString(Constants.KEY_PREVIOUS_PUNCH_REQUEST).equals(previousValue)) {
                notificationManager.notify(609, builder.build());
            }

            preferencesManager.putString(Constants.KEY_PREVIOUS_PUNCH_REQUEST, previousValue);
        }

    }

    private void sendNotification(String title, String messageBody, int channelId) {
        Intent intent = new Intent(context, MainPageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setSound(soundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(600 + channelId, notificationBuilder.build());

    }

}
