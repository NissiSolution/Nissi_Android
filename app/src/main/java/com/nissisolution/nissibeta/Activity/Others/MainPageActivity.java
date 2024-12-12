package com.nissisolution.nissibeta.Activity.Others;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Activity.GuestHouse.GuestHouseActivity;
import com.nissisolution.nissibeta.Activity.HumanResources.HrActivity;
import com.nissisolution.nissibeta.Activity.Punching.CheckInOutActivity;
import com.nissisolution.nissibeta.Activity.RandL.LoginActivity;
import com.nissisolution.nissibeta.Activity.Settings.SettingsActivity;
import com.nissisolution.nissibeta.BuildConfig;
import com.nissisolution.nissibeta.CustomView.CustomCalendar;
import com.nissisolution.nissibeta.Notification.ForegroundService;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.InAppUpdate;
import com.nissisolution.nissibeta.Supports.Supports;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity {

    private ImageView checked_i, guest_house_i, support_i, sos_i, hr_i;
    private TextView checked_t, guest_house_t, support_t, sos_t, licence, hr_t;
    private long pressedTime;
    private PreferencesManager preferencesManager, preferencesManager2;
    private ProgressBar progressBar;
    private Dialog dialog;
    private String check_in_url, staff_id, password;
    private Context context;
    private InAppUpdate inAppUpdate;
    private Supports supports;
    private CustomCalendar custom_calendar;
    private TextView version;
    private boolean canGet;
    private AlarmManager alarmManager;
    private boolean isActive = false;
    private RelativeLayout hr_count_layout;
    private TextView hr_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);

        declare_item();
        set_listeners();

        try {
            inAppUpdate.checkForAppUpdate();
        } catch (Exception ignored) {

        }
        if (password != null && staff_id != null) {
            checkPassword();
        } else {
            Intent intent = new Intent(MainPageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermission();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmPermission();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void notificationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void alarmPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 101);
        }
    }

    private void declare_item() {
        inAppUpdate = new InAppUpdate(MainPageActivity.this);
        checked_i = findViewById(R.id.check_in_image);
        guest_house_i = findViewById(R.id.guest_house_image);
        support_i = findViewById(R.id.support_image);
        sos_i = findViewById(R.id.sos_image);
        hr_i = findViewById(R.id.hr_image);

        checked_t = findViewById(R.id.check_in_text);
        guest_house_t = findViewById(R.id.guest_house_text);
        support_t = findViewById(R.id.support_text);
        sos_t = findViewById(R.id.sos_text);
        licence = findViewById(R.id.licence);
        hr_t = findViewById(R.id.hr_text);

        hr_count = findViewById(R.id.hr_count);
        hr_count_layout = findViewById(R.id.hr_count_layout);

        progressBar = findViewById(R.id.progressBar);
        dialog = new Dialog(this);

        preferencesManager = new PreferencesManager(this);
        context = MainPageActivity.this;
        preferencesManager2 = new PreferencesManager(context, Constants.KEY_NOTIFICATION);

        check_in_url = Constants.KEY_DATABASE_LINK;
        staff_id = preferencesManager.getString(Constants.KEY_STAFF_ID);
        password = preferencesManager.getString(Constants.KEY_PASSWORD);
        supports = new Supports(context);
        custom_calendar = findViewById(R.id.custom_calender);
        version = findViewById(R.id.version);
        canGet = false;

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @SuppressLint("SetTextI18n")
    private void set_listeners() {
//        supports.start_activity_listener(checked_i, Check_in_outActivity.class);
//        supports.start_activity_listener(guest_house_i, Guest_house_Activity.class);
//        supports.start_activity_listener(hr_i, Hr_Activity.class);
//        supports.start_activity_listener(checked_t, Check_in_outActivity.class);
//        supports.start_activity_listener(guest_house_t, Guest_house_Activity.class);
//        supports.start_activity_listener(hr_t, Hr_Activity.class);
//
//        support_i.setOnClickListener(view -> supports.start_activity(Dummy_Activity.class));

        // support_i.setOnClickListener(view -> supports.start_activity(SupportActivity.class));

        // sos_i.setOnClickListener(view -> supports.start_activity(SosActivity.class));

        // support_t.setOnClickListener(view -> supports.start_activity(SupportActivity.class));

        // sos_t.setOnClickListener(view -> supports.start_activity(SosActivity.class));

        supports.licence(licence);

        custom_calendar.setData(context, progressBar, preferencesManager.getString(Constants.KEY_STAFF_ID));
        custom_calendar.setMonthYear(Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.YEAR));
        custom_calendar.setDatesBackground(R.color.lighter_blue);
        custom_calendar.setDaysTextColor(getResources().getColor(R.color.nissi_blue));
        custom_calendar.setHeaderTextColor(getResources().getColor(R.color.white));
        custom_calendar.setHeaderBackground(getResources().getColor(R.color.nissi_blue));
        custom_calendar.displayContent();
        version.setText("Version: " + BuildConfig.VERSION_NAME);
    }

    private void checkPassword() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handlePassword,
                error -> {
                    progressBar.setVisibility(View.GONE);
                    supports.create_short_toast(Constants.KEY_SERVER_ERROR);
                    dialog.setContentView(R.layout.dialog_unable_to_connect);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button reconnect = dialog.findViewById(R.id.reconnect);
                    reconnect.setOnClickListener(view -> {
                        checkPassword();
                        dialog.dismiss();
                    });
                    Button exits = dialog.findViewById(R.id.exit);
                    exits.setOnClickListener(view -> finish());
                    progressBar.setVisibility(View.GONE);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_CHECK_PASSWORD);
                params.put(Constants.KEY_STAFF_ID, staff_id);
                params.put(Constants.KEY_PASSWORD, password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handlePassword(String response) {
        progressBar.setVisibility(View.GONE);
        if (response.equals(Constants.KEY_SUCCESS)) {
            supports.start_activity_listener(checked_i, CheckInOutActivity.class);
            supports.start_activity_listener(guest_house_i, GuestHouseActivity.class);
            supports.start_activity_listener(hr_i, HrActivity.class);
            supports.start_activity_listener(checked_t, CheckInOutActivity.class);
            supports.start_activity_listener(guest_house_t, GuestHouseActivity.class);
            supports.start_activity_listener(hr_t, HrActivity.class);
            load_data(staff_id);
            setNotifications();
        } else if (response.equals(Constants.KEY_FAILURE)) {
            Intent intent = new Intent(MainPageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            supports.create_short_toast(Constants.KEY_BACK_PRESSED_MESSAGE);
        }
        pressedTime = System.currentTimeMillis();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_logout) {
            preferencesManager.clear();
            supports.start_activity(LoginActivity.class);
            finish();
        }
        if (id == R.id.item_guest_house_location) {
            supports.start_activity(GuestHouseActivity.class);
        }
        if (id == R.id.item_settings) {
            supports.start_activity(SettingsActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    public void load_data(String s_id) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_in_url,
                this::set_data,
                error -> {
                    progressBar.setVisibility(View.GONE);
                    supports.create_short_toast(Constants.KEY_SERVER_ERROR);
                    dialog.setContentView(R.layout.dialog_unable_to_connect);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button reconnect = dialog.findViewById(R.id.reconnect);
                    reconnect.setOnClickListener(view -> {
                        load_data(s_id);
                        dialog.dismiss();
                    });
                    Button exits = dialog.findViewById(R.id.exit);
                    exits.setOnClickListener(view -> finish());
                    progressBar.setVisibility(View.GONE);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_FETCH_DATA);
                params.put(Constants.KEY_STAFF_ID, s_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainPageActivity.this);
        requestQueue.add(stringRequest);
    }

    public void set_data(String response) {
        String[] data = response.split("-");
        progressBar.setVisibility(View.GONE);
        supports.create_short_toast(Constants.KEY_WELCOME + data[0]);
        preferencesManager.putString(Constants.KEY_STAFF_NAME, data[0]);
        preferencesManager.putString(Constants.KEY_EMPLOYEE_ID, data[1]);
        getMyDetailsInfo();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            setNotifications();
        }
        inAppUpdate.onActivityResult(requestCode, resultCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        inAppUpdate.onResume();
        currentDateInfo();
        checkNotification();
        isActive = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inAppUpdate.onDestroy();
        setNotifications();
        isActive = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isActive = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    private void getMyDetailsInfo() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handleMyDetails, error -> {
                progressBar.setVisibility(View.GONE);
                supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_MY_DETAILS_INFO);
                params.put(Constants.KEY_DATE, get_double(Calendar.getInstance().get(Calendar.YEAR)) + "-" + get_double(Calendar.getInstance().get(Calendar.MONTH) + 1));
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void handleMyDetails(String response) {
        progressBar.setVisibility(View.GONE);
        List<String> list_1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_1));
        for (int i=1; i <= Calendar.getInstance().get(Calendar.DATE) - 1; i++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
            if (list_2.get(0).equals(Constants.KEY_0)) {
                if (!get_day(getTimestamp(list_2.get(1))).equals("Sun")) {
                    custom_calendar.setHighlighter(getTimestamp(list_2.get(1)), getResources().getDrawable(R.drawable.background_working), getResources().getColor(R.color.white));
                } else {
                    custom_calendar.setHighlighter(getTimestamp(list_2.get(1)), getResources().getDrawable(R.drawable.background_holiday), getResources().getColor(R.color.white));
                }
            } else {
                switch (list_2.get(0)) {
                    case "W":
                    case "CO":
                    case "LH":
                        custom_calendar.setHighlighter(getTimestamp(list_2.get(1)), getResources().getDrawable(R.drawable.background_present), getResources().getColor(R.color.nissi_blue));
                        break;
                    case "HO":
                        custom_calendar.setHighlighter(getTimestamp(list_2.get(1)), getResources().getDrawable(R.drawable.background_holiday), getResources().getColor(R.color.white));
                        break;
                    case "LOP":
                        custom_calendar.setHighlighter(getTimestamp(list_2.get(1)), getResources().getDrawable(R.drawable.background_working), getResources().getColor(R.color.white));
                        break;
                    default:
                        custom_calendar.setHighlighter(getTimestamp(list_2.get(1)), getResources().getDrawable(R.drawable.background_leave), getResources().getColor(R.color.nissi_blue));
                        break;
                }
            }
        }
        canGet = true;
        currentDateInfo();
        checkNotification();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void currentDateInfo() {
        if (canGet && !get_day(new Date().getTime()).equals("Sun")) {
            StringRequest request = new StringRequest(Request.Method.POST, check_in_url,
                    this::handleCurrentDate, volleyError -> {
                supports.create_short_toast("error");
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_CURRENT_DATE_CHECK);
                    params.put(Constants.KEY_STAFF_ID, staff_id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        } else if (canGet) {
            custom_calendar.setHighlighter(new Date().getTime(), getResources().getDrawable(R.drawable.background_current_date_holiday), getResources().getColor(R.color.white));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void handleCurrentDate(String response) {
        List<String> list1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_2));
        switch (list1.get(0)) {
            case "W":
            case "CO":
            case "LH":
                custom_calendar.setHighlighter(getTimestamp(list1.get(1)), getResources().getDrawable(R.drawable.background_current_date_present), getResources().getColor(R.color.nissi_blue));
                break;
            case "HO":
                custom_calendar.setHighlighter(getTimestamp(list1.get(1)), getResources().getDrawable(R.drawable.background_current_date_holiday), getResources().getColor(R.color.white));
                break;
            case "LOP":
                custom_calendar.setHighlighter(getTimestamp(list1.get(1)), getResources().getDrawable(R.drawable.background_current_date_lop), getResources().getColor(R.color.white));
                break;
            case Constants.KEY_0:
                custom_calendar.setHighlighter(getTimestamp(list1.get(1)), getResources().getDrawable(R.drawable.background_current_date_working), getResources().getColor(R.color.white));
                break;
            default:
                custom_calendar.setHighlighter(getTimestamp(list1.get(1)), getResources().getDrawable(R.drawable.background_current_date_leave), getResources().getColor(R.color.nissi_blue));
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private long getTimestamp(String the_value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(the_value).getTime();
        } catch (Exception ignored) {
            return 0;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String get_day(long the_value) {
        SimpleDateFormat format = new SimpleDateFormat("E");
        return format.format(new Date(the_value));
    }

    private String get_double(int the_value) {
        if (the_value < 10) {
            return "0" + the_value;
        } else {
            return String.valueOf(the_value);
        }
    }

    private void setNotifications() {
        try {
            Intent intent = new Intent(context, ForegroundService.class);
            intent.putExtra(Constants.KEY_DATA, Constants.KEY_DATA);
            context.startService(intent);
        } catch (Exception e) {

        }
    }

    private void checkNotification() {
        try {
            if (isActive) {
                StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                        this::handleNotification,
                        error -> {
                            new Handler().postDelayed(this::checkNotification, 60 * 1000);
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
            }
        } catch (Exception ignored) {

        }
    }

    private void handleNotification(String response) {

        List<String> list1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_1));

        int count = 0;

        for (int i = 1; i < list1.size(); i++) {
            List<String> list2 = Arrays.asList(list1.get(i).split(Constants.KEY_SPLITTER_2));
            if (Integer.parseInt(list2.get(5)) == 0) {
                count++;
            }
        }

        if (count > 0) {
            hr_count_layout.setVisibility(View.VISIBLE);
            hr_count.setText(String.valueOf(count));
        } else {
            hr_count_layout.setVisibility(View.GONE);
        }

        new Handler().postDelayed(this::checkNotification, 60 * 1000);
    }

}