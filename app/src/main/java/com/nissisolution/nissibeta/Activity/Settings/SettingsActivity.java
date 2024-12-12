package com.nissisolution.nissibeta.Activity.Settings;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.imageview.ShapeableImageView;
import com.nissisolution.nissibeta.Notification.NotificationReceiver;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Context context;
    private Supports supports;
    private PreferencesManager preferencesManager2;
    private LinearLayout punchInOutReminderLayout, punchInReminderLayout, punchOutReminderLayout;
    private Switch punchInOutReminder, punchInReminder, punchOutReminder;
    private TextView punchInReminderTime, punchOutReminderTime;
    private ShapeableImageView punchInReminderClock, punchOutReminderClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        declareItem();
        setListener();
        setInitial();
        toolbar();
    }

    private void declareItem() {
        context = this;
        supports = new Supports(context);
        preferencesManager2 = new PreferencesManager(context, Constants.KEY_NOTIFICATION);
        punchInOutReminderLayout = findViewById(R.id.punchInOutReminderLayout);
        punchInReminderLayout = findViewById(R.id.punchInReminderLayout);
        punchOutReminderLayout = findViewById(R.id.punchOutReminderLayout);
        punchInOutReminder = findViewById(R.id.punchInOutReminder);
        punchInReminder = findViewById(R.id.punchInReminder);
        punchOutReminder = findViewById(R.id.punchOutReminder);
        punchInReminderTime = findViewById(R.id.punchInReminderTime);
        punchOutReminderTime = findViewById(R.id.punchOutReminderTime);
        punchInReminderClock = findViewById(R.id.punchInReminderClock);
        punchOutReminderClock = findViewById(R.id.punchOutReminderClock);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setListener() {
        punchInOutReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager2.putBoolean(Constants.KEY_PUNCH_NOTIFICATION, isChecked);
            if (preferencesManager2.getBoolean(Constants.KEY_PUNCH_NOTIFICATION)) {
                punchInOutReminder.setChecked(true);
                punchInOutReminderLayout.setVisibility(View.VISIBLE);
            } else {
                punchInOutReminder.setChecked(false);
                punchInOutReminderLayout.setVisibility(View.GONE);
            }
        });

        punchInReminder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            preferencesManager2.putBoolean(Constants.KEY_IN_TIME, isChecked);
            if (preferencesManager2.getBoolean(Constants.KEY_IN_TIME)) {
                punchInReminderClock.setImageDrawable(getDrawable(R.drawable.vector_alarm));
            } else {
                punchInReminderClock.setImageDrawable(getDrawable(R.drawable.vector_alarm_off));
            }
        }));

        punchOutReminder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            preferencesManager2.putBoolean(Constants.KEY_OUT_TIME, isChecked);
            if (preferencesManager2.getBoolean(Constants.KEY_OUT_TIME)) {
                punchOutReminderClock.setImageDrawable(getDrawable(R.drawable.vector_alarm));
            } else {
                punchOutReminderClock.setImageDrawable(getDrawable(R.drawable.vector_alarm_off));
            }
        }));
        
        timePicker(punchInReminderLayout, punchInReminderTime, Constants.KEY_IN_TIME, Constants.KEY_IN_TIME_S, Constants.KEY_LAST_IN_DATE);
        timePicker(punchOutReminderLayout, punchOutReminderTime, Constants.KEY_OUT_TIME, Constants.KEY_OUT_TIME_S, Constants.KEY_LAST_OUT_DATE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setInitial() {
        if (preferencesManager2.getBoolean(Constants.KEY_PUNCH_NOTIFICATION)) {
            punchInOutReminder.setChecked(true);
            punchInOutReminderLayout.setVisibility(View.VISIBLE);
        } else {
            punchInOutReminder.setChecked(false);
            punchInOutReminderLayout.setVisibility(View.GONE);
        }
        if (preferencesManager2.getBoolean(Constants.KEY_IN_TIME)) {
            punchInReminder.setChecked(true);
            punchInReminderClock.setImageDrawable(getDrawable(R.drawable.vector_alarm));
        } else {
            punchInReminder.setChecked(false);
            punchInReminderClock.setImageDrawable(getDrawable(R.drawable.vector_alarm_off));
        }
        if (preferencesManager2.getString(Constants.KEY_IN_TIME_S) != null) {
            punchInReminderTime.setText(get12HrTimeS(get24HrTime(preferencesManager2.getString(Constants.KEY_IN_TIME_S))));
        }
        if (preferencesManager2.getBoolean(Constants.KEY_OUT_TIME)) {
            punchOutReminder.setChecked(true);
            punchOutReminderClock.setImageDrawable(getDrawable(R.drawable.vector_alarm));
        } else {
            punchOutReminder.setChecked(false);
            punchOutReminderClock.setImageDrawable(getDrawable(R.drawable.vector_alarm_off));
        }
        if (preferencesManager2.getString(Constants.KEY_OUT_TIME_S) != null) {
            punchOutReminderTime.setText(get12HrTimeS(get24HrTime(preferencesManager2.getString(Constants.KEY_OUT_TIME_S))));
        }
    }

    private void toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void timePicker(LinearLayout layout, TextView element, String location, String location2, String location3) {
        layout.setOnClickListener(v -> {
            try {
                if (preferencesManager2.getBoolean(location)) {
                    Calendar mCurrentTime = Calendar.getInstance();
                    int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mCurrentTime.get(Calendar.MINUTE);
                    try {
                        List<String> timeList = Arrays.asList(preferencesManager2.getString(location2).split(":"));
                        hour = Integer.parseInt(timeList.get(0));
                        minute = Integer.parseInt(timeList.get(1));
                    } catch (Exception ignored) {

                    }
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            element.setText(get12HrTimeS(get24HrTime(selectedHour + ":" + selectedMinute)));
                            preferencesManager2.putString(location2, (selectedHour + ":" + selectedMinute));
                            preferencesManager2.putString(location3, previousDate());
                        }
                    }, hour, minute, false);
                    mTimePicker.show();
                }
            } catch (Exception ignored) {

            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private long get24HrTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String get24HrTimeS(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(timestamp);
    }

    @SuppressLint("SimpleDateFormat")
    private long get12HrTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String get12HrTimeS(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(timestamp);
    }

    @SuppressLint("SimpleDateFormat")
    private String previousDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return format.format(calendar.getTime());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (preferencesManager2.getBoolean(Constants.KEY_PUNCH_NOTIFICATION) && preferencesManager2.getBoolean(Constants.KEY_IN_TIME) && preferencesManager2.getString(Constants.KEY_IN_TIME_S) != null) {
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent punchInIntent = new Intent(context, NotificationReceiver.class);
                punchInIntent.putExtra(Constants.KEY_TYPE, Constants.KEY_0);
                PendingIntent punchInPendingIntent = PendingIntent.getBroadcast(context, 0, punchInIntent.putExtra(Constants.KEY_TYPE, Constants.KEY_SPLITTER_4 + Constants.KEY_0 + Constants.KEY_SPLITTER_4), PendingIntent.FLAG_IMMUTABLE);

                List<String> list1 = Arrays.asList(preferencesManager2.getString(Constants.KEY_IN_TIME_S).split(":"));

                Calendar inCalendar = Calendar.getInstance();
                inCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(list1.get(0)));
                inCalendar.set(Calendar.MINUTE, Integer.parseInt(list1.get(1)));
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, inCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, punchInPendingIntent);
            }

            if (preferencesManager2.getBoolean(Constants.KEY_PUNCH_NOTIFICATION) && preferencesManager2.getBoolean(Constants.KEY_OUT_TIME) && preferencesManager2.getString(Constants.KEY_OUT_TIME_S) != null) {
//                AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent punchOutIntent = new Intent(context, NotificationReceiver.class);
                PendingIntent punchOutPendingIntent = PendingIntent.getBroadcast(context, 1, punchOutIntent.putExtra(Constants.KEY_TYPE, Constants.KEY_SPLITTER_4 + Constants.KEY_1 + Constants.KEY_SPLITTER_4), PendingIntent.FLAG_IMMUTABLE);

                List<String> list1 = Arrays.asList(preferencesManager2.getString(Constants.KEY_OUT_TIME_S).split(":"));

                Calendar outCalendar = Calendar.getInstance();
                outCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(list1.get(0)));
                outCalendar.set(Calendar.MINUTE, Integer.parseInt(list1.get(1)));
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, outCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, punchOutPendingIntent);
            }
        } catch (Exception ignored) {

        }
    }
}