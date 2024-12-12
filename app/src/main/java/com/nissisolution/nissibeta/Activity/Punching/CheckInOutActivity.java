package com.nissisolution.nissibeta.Activity.Punching;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.nissisolution.nissibeta.Activity.CompOffHoliday.CompOffHolidayActivity;
import com.nissisolution.nissibeta.Activity.NightShift.NightShiftActivity;
import com.nissisolution.nissibeta.Adapter.CheckInOut.CheckInOutAdapter;
import com.nissisolution.nissibeta.Notification.NotificationReceiver;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Supports;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckInOutActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView name, id, date_now, licence;
    private Button check_in, check_out, send_request;
    private String check_process, night_process, check_in_url, staff_id, request_type, the_date;
    private long pressedTime;
    private PreferencesManager preferencesManager, preferencesManager2;
    private Dialog dialog;
    private Context context;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CheckInOutAdapter check_in_out_adapter;
    private ImageView eye;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Supports supports;
    private Spinner type_of;
    private ArrayAdapter<String> type_of_adapter;
    private LinearLayout calender_layout;
    private RelativeLayout layout_1, check_in_out_layout, request_layout, nightShiftLayout;
    private CalendarView calendarView;
    private TextInputEditText reason;
    private DigitalClock clock;
    private RadioGroup leave_type, leave_type2;
    private RadioButton punch_in_out, comp_off, local_holiday, night_shift;
    private boolean canSend1 = true, canSend2 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_check_in_out);

        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.check_in_out));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        declare_item();
        set_listeners();
        set_spinner();

        load_data(staff_id);

        the_date = get_date_format_2(date());
        date_now.setText(date());

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(CheckInOutActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        check_location_permission();

        layout_1.setVisibility(View.VISIBLE);
        calender_layout.setVisibility(View.GONE);
        // check_in_out_layout.setVisibility(View.VISIBLE);
        request_layout.setVisibility(View.GONE);
        request_type = Constants.KEY_10;
        date_now.setText(date());
        check_is_date_exists();
        clock.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void declare_item() {
        name = findViewById(R.id.name);
        id = findViewById(R.id.employee_id);
        licence = findViewById(R.id.licence);
        check_in = findViewById(R.id.check_in);
        check_out = findViewById(R.id.check_out);
        date_now = findViewById(R.id.date);
        preferencesManager = new PreferencesManager(this);
        preferencesManager2 = new PreferencesManager(this, Constants.KEY_NOTIFICATION);
        context = this;
        dialog = new Dialog(context);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        eye = findViewById(R.id.eye);
        type_of = findViewById(R.id.type_of);
        calender_layout = findViewById(R.id.calender_layout);
        layout_1 = findViewById(R.id.layout_1);
        check_in_out_layout = findViewById(R.id.check_in_out_layout);
        request_layout = findViewById(R.id.request_layout);
        calendarView = findViewById(R.id.calender_view);
        send_request = findViewById(R.id.send_request);
        clock = findViewById(R.id.time_now);
        nightShiftLayout = findViewById(R.id.night_shift_layout);

        supports = new Supports(context);

        check_in.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lighter_green)));
        check_in.setEnabled(false);
        check_out.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
        check_out.setEnabled(false);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        check_in_url = Constants.KEY_DATABASE_LINK;
        staff_id = preferencesManager.getString(Constants.KEY_STAFF_ID);
        reason = findViewById(R.id.reason);

        leave_type = findViewById(R.id.leave_type);
        leave_type2 = findViewById(R.id.leave_type2);
        punch_in_out = findViewById(R.id.punch_in_out);
        comp_off = findViewById(R.id.comp_off);
        local_holiday = findViewById(R.id.local_holiday);
        night_shift = findViewById(R.id.night_shift);

        name.setText(preferencesManager.getString(Constants.KEY_STAFF_NAME));
        id.setText(preferencesManager.getString(Constants.KEY_EMPLOYEE_ID));
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables", "SetTextI18n"})
    private void set_listeners() {
        supports.start_activity_listener(eye, MapsActivity.class);

        supports.licence(licence);

        check_in.setOnClickListener(view -> {
            if (location_permission()) {
                get_location(Constants.KEY_1);
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, Constants.KEY_CONNECT_LOCATION_ERROR, Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(CheckInOutActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        });

        check_out.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (location_permission()) {
                get_location(Constants.KEY_2);
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, Constants.KEY_CONNECT_LOCATION_ERROR, Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(CheckInOutActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        });

        leave_type.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.punch_in_out:
                    if (punch_in_out.isChecked()) {
                        check_in_out_layout.setVisibility(View.GONE);
                        layout_1.setVisibility(View.VISIBLE);
                        calender_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.GONE);
                        request_type = Constants.KEY_10;
                        date_now.setText(date());
                        the_date = get_date_format_2(date());
                        check_is_date_exists();
                        clock.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        nightShiftLayout.setVisibility(View.GONE);
                        leave_type2.clearCheck();
                    }
                    break;
                case R.id.comp_off:
                    if (comp_off.isChecked()) {
                        check_in_out_layout.setVisibility(View.GONE);
                        layout_1.setVisibility(View.GONE);
                        calender_layout.setVisibility(View.VISIBLE);
                        request_layout.setVisibility(View.VISIBLE);
                        request_type = Constants.KEY_1;
                        check_is_date_exists();
                        clock.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        nightShiftLayout.setVisibility(View.GONE);
                        leave_type2.clearCheck();
                    }
                    break;
                case R.id.local_holiday:
                    if (local_holiday.isChecked()) {
                        layout_1.setVisibility(View.GONE);
                        calender_layout.setVisibility(View.VISIBLE);
                        check_in_out_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.VISIBLE);
                        request_type = Constants.KEY_2;
                        clock.setVisibility(View.GONE);
                        check_is_date_exists();
                        recyclerView.setVisibility(View.GONE);
                        nightShiftLayout.setVisibility(View.GONE);
                        leave_type2.clearCheck();
                    }
                    break;
                case  R.id.night_shift:
                    if (night_shift.isChecked()) {
                        check_in_out_layout.setVisibility(View.GONE);
                        layout_1.setVisibility(View.VISIBLE);
                        calender_layout.setVisibility(View.VISIBLE);
                        request_layout.setVisibility(View.VISIBLE);
                        request_type = Constants.KEY_3;
                        check_is_date_exists();
                        clock.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        nightShiftLayout.setVisibility(View.GONE);
                        leave_type2.clearCheck();
                    }
                    break;
                default:
                    break;
            }
        });

        leave_type2.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.punch_in_out:
                    if (punch_in_out.isChecked()) {
                        check_in_out_layout.setVisibility(View.GONE);
                        layout_1.setVisibility(View.VISIBLE);
                        calender_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.GONE);
                        request_type = Constants.KEY_10;
                        date_now.setText(date());
                        the_date = get_date_format_2(date());
                        check_is_date_exists();
                        clock.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        nightShiftLayout.setVisibility(View.GONE);
                        leave_type.clearCheck();
                    }
                    break;
                case R.id.comp_off:
                    if (comp_off.isChecked()) {
                        check_in_out_layout.setVisibility(View.GONE);
                        layout_1.setVisibility(View.GONE);
                        calender_layout.setVisibility(View.VISIBLE);
                        request_layout.setVisibility(View.VISIBLE);
                        request_type = Constants.KEY_1;
                        check_is_date_exists();
                        clock.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        nightShiftLayout.setVisibility(View.GONE);
                        leave_type.clearCheck();
                    }
                    break;
                case R.id.local_holiday:
                    if (local_holiday.isChecked()) {
                        layout_1.setVisibility(View.GONE);
                        calender_layout.setVisibility(View.VISIBLE);
                        check_in_out_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.VISIBLE);
                        request_type = Constants.KEY_2;
                        clock.setVisibility(View.GONE);
                        check_is_date_exists();
                        recyclerView.setVisibility(View.GONE);
                        nightShiftLayout.setVisibility(View.GONE);
                        leave_type.clearCheck();
                    }
                    break;
                case  R.id.night_shift:
                    //todo
                    if (night_shift.isChecked()) {
                        check_in_out_layout.setVisibility(View.GONE);
                        layout_1.setVisibility(View.VISIBLE);
                        calender_layout.setVisibility(View.VISIBLE);
                        request_layout.setVisibility(View.VISIBLE);
                        request_type = Constants.KEY_3;
                        check_is_date_exists();
                        clock.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        nightShiftLayout.setVisibility(View.GONE);
                        leave_type.clearCheck();
                    }
                    break;
                default:
                    break;
            }
        });

        type_of.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        layout_1.setVisibility(View.VISIBLE);
                        calender_layout.setVisibility(View.GONE);
                        check_in_out_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.GONE);
                        date_now.setText(date());
                        the_date = get_date_format_2(date());
                        calendarView.setDate(new Date().getTime());
                        request_type = Constants.KEY_0;
                        clock.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        layout_1.setVisibility(View.VISIBLE);
                        calender_layout.setVisibility(View.GONE);
                        check_in_out_layout.setVisibility(View.VISIBLE);
                        request_layout.setVisibility(View.GONE);
                        request_type = Constants.KEY_10;
                        date_now.setText(date());
                        check_is_date_exists();
                        clock.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        layout_1.setVisibility(View.GONE);
                        calender_layout.setVisibility(View.VISIBLE);
                        check_in_out_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.VISIBLE);
                        request_type = Constants.KEY_1;
                        check_is_date_exists();
                        clock.setVisibility(View.GONE);
                        break;
                    case 3:
                        layout_1.setVisibility(View.GONE);
                        calender_layout.setVisibility(View.VISIBLE);
                        check_in_out_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.VISIBLE);
                        request_type = Constants.KEY_2;
                        clock.setVisibility(View.GONE);
                        check_is_date_exists();
                        break;
                    default:
                        layout_1.setVisibility(View.VISIBLE);
                        calender_layout.setVisibility(View.GONE);
                        check_in_out_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (min_date() != 0) {
            calendarView.setMinDate(min_date());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();
        calendarView.setMaxDate(lastDayOfMonth.getTime());

        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> set_selected_date(i2, i1+1, i));

        send_request.setOnClickListener(view -> {
            check_condition_for_holiday_and_comp_off(request_type);
        });

        supports.edit_text_watcher(reason);

    }

    @SuppressLint("SimpleDateFormat")
    private String previous_date() {
        Date yesterdayDate = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(yesterdayDate);
    }

    @SuppressLint("SetTextI18n")
    private void set_selected_date(int date, int month, int year) {
        String d, m, y;
        if (date < 10) {
            d = "0" + date;
        } else {
            d = String.valueOf(date);
        }

        if (month < 10) {
            m = "0" + month;
        } else {
            m = String.valueOf(month);
        }

        y = String.valueOf(year);

        the_date = y + "-" + m + "-" + d;
        date_now.setText(d+"-"+m+"-"+y);
        check_is_date_exists();
    }

    private void set_spinner() {
        type_of_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout,
                getResources().getStringArray(R.array.type_of));
        type_of.setAdapter(type_of_adapter);
    }

    private String date() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long n_date = new Date().getTime();
        return simpleDateFormat.format(n_date);
    }

    private long min_date() {
        List<String> n_date = Arrays.asList(date().split("-"));
        String s_d = "01-" + n_date.get(1) + "-" + n_date.get(2);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date d_d = format.parse(s_d);
            return d_d.getTime();
        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public void load_data(String s_id) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_in_url,
                response -> {
                    set_data(response);
                    get_check_in_out();
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CheckInOutActivity.this, Constants.KEY_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    dialog.setContentView(R.layout.dialog_unable_to_connect);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button reconnect = dialog.findViewById(R.id.reconnect);
                    reconnect.setOnClickListener(view -> {
                        load_data(s_id);
                        dialog.dismiss();
                    });
                    Button exits = dialog.findViewById(R.id.exit);
                    exits.setOnClickListener(view -> onBackPressed());
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
        RequestQueue requestQueue = Volley.newRequestQueue(CheckInOutActivity.this);
        requestQueue.add(stringRequest);
    }

    public void set_data(String response) {
        String[] data = response.split("-");
        name.setText(data[0]);
        id.setText(data[1]);
        progressBar.setVisibility(View.GONE);
    }

    private void check_in_out(String type_check, String latitude, String longitude) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_in_url,
                response -> {
                    if (response.equals(Constants.KEY_ERROR)) {
                        Toast.makeText(CheckInOutActivity.this, Constants.KEY_SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        canSend1 = false;
                    } else {
                        show_dialog(response);
                        sendNotification(type_check);
                    }
                },
                error -> {
                    canSend1 = true;
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CheckInOutActivity.this, Constants.KEY_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_CHECK_IN_OUT);
                params.put(Constants.KEY_STAFF_ID, staff_id);
                params.put(Constants.KEY_TYPE_CHECK, type_check);
                params.put(Constants.KEY_TYPE, Constants.KEY_W);
                params.put(Constants.KEY_ROUTE_POINT_ID, Constants.KEY_0);
                params.put(Constants.KEY_WORK_PLACE_ID, Constants.KEY_0);
                params.put(Constants.KEY_LONGITUDE, longitude);
                params.put(Constants.KEY_LATITUDE, latitude);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CheckInOutActivity.this);
        requestQueue.add(stringRequest);
    }

    private void show_dialog(String value) {
        canSend1 = true;
        if (value.equals(Constants.KEY_1)) {
            dialog.setContentView(R.layout.dialog_checked_in);
            Button ok = dialog.findViewById(R.id.ok);
            ok.setOnClickListener(view -> dialog.dismiss());
            progressBar.setVisibility(View.GONE);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            preferencesManager2.putString(Constants.KEY_LAST_IN_DATE, Constants.getDate(new Date().getTime()));
        } else if (value.equals(Constants.KEY_2)) {
            dialog.setContentView(R.layout.dialog_checked_out);
            Button ok = dialog.findViewById(R.id.ok);
            ok.setOnClickListener(view -> dialog.dismiss());
            progressBar.setVisibility(View.GONE);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            preferencesManager2.putString(Constants.KEY_LAST_OUT_DATE, Constants.getDate(new Date().getTime()));
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
        }
        get_check_in_out();
    }

    private void get_check_in_out() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_in_url,
                response -> {
                    show_data_to_recyclerview(response);
                    progressBar.setVisibility(View.GONE);

                    if (!response.equals(" ")) {
                        show_data_to_recyclerview(response);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CheckInOutActivity.this, Constants.KEY_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_CHECK_IN_OUT);
                params.put(Constants.KEY_STAFF_ID, staff_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CheckInOutActivity.this);
        requestQueue.add(stringRequest);
    }


    private void show_data_to_recyclerview(String response) {
        if (!response.equals("")) {
            List<String> data = Arrays.asList(response.split("&"));
            check_in_out_adapter = new CheckInOutAdapter(data);
            recyclerView.setAdapter(check_in_out_adapter);
            List<String> last_arr = Arrays.asList(data.get(data.size() - 1).split("--"));
            check_process = last_arr.get(1);
        } else {
            check_process = Constants.KEY_2;
        }

        show_buttons();
    }

    private void show_buttons() {
        if (check_process.equals(Constants.KEY_1)) {
            check_in.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lighter_green)));
            check_in.setEnabled(false);
            check_out.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            check_out.setEnabled(true);
        } else if (check_process.equals(Constants.KEY_2)) {
            check_in.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            check_in.setEnabled(true);
            check_out.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_red)));
            check_out.setEnabled(false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void get_location(String type_check) {
        Location myLocation = map.getMyLocation();

        if (myLocation != null) {
            if (canSend1) {
                canSend1 = false;
                check_in_out(type_check, String.valueOf(myLocation.getLatitude()), String.valueOf(myLocation.getLongitude()));
            }
        } else {
            supports.create_short_toast(Constants.KEY_CONNECT_LOCATION_ERROR);
        }
    }

    private void check_location_permission() {
        if (ActivityCompat.checkSelfPermission(CheckInOutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CheckInOutActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    private boolean location_permission() {
        return ActivityCompat.checkSelfPermission(CheckInOutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            check_location_permission();
            return;
        }
        map.setMyLocationEnabled(true);

        new Handler().postDelayed(this::show_my_location, 1000);

        upload_times(preferencesManager.getString(Constants.KEY_STAFF_ID));

    }

    private void show_my_location() {
        try {
            Location location = map.getMyLocation();
            if (location != null) {
                LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(17).bearing(0).tilt(30).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
            }
        }catch (Exception ignored) {

        }
    }

    private void upload_times(String s_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_in_url,
                response -> {

                },
                error -> {

                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_SET_TIMES);
                params.put(Constants.KEY_STAFF_ID, s_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CheckInOutActivity.this);
        requestQueue.add(stringRequest);
    }

    private void send_holiday_or_comp_off(String the_type, String the_reason) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> handle_holiday_comp_off_response(response, the_type),
                error -> {
                    canSend2 = true;
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    progressBar.setVisibility(View.GONE);
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_HOLIDAY_OR_COMP_OFF);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_FROM_DATE, the_date);
                params.put(Constants.KEY_REASON, the_reason);
                params.put(Constants.KEY_TYPE, the_type);
                params.put(Constants.KEY_TIMESTAMP, String.valueOf(new Date().getTime()));
                params.put(Constants.KEY_WORK_HOURS, Constants.KEY_10);
                params.put(Constants.KEY_FROM_TIME, Constants.KEY_FROM_TIME);
                params.put(Constants.KEY_TO_TIME, Constants.KEY_TO_TIME);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void handle_holiday_comp_off_response(String the_response, String the_type) {
        supports.create_short_toast(Constants.KEY_DATA_SEND_SUCCESSFULLY);
        progressBar.setVisibility(View.GONE);
        reason.setText(null);
        send_request.setEnabled(false);
        send_request.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.signature_blue)));
        if (the_type.equals(Constants.KEY_3)) {
            supports.start_activity(NightShiftActivity.class);
        } else {
            supports.start_activity(CompOffHolidayActivity.class);
        }
        finish();
    }

    private void check_condition_for_holiday_and_comp_off(String the_type) {
        String s_reason = reason.getText().toString().trim();
        if (s_reason.isEmpty()) {
            reason.setError(Constants.KEY_REQUIRED);
            reason.requestFocus();
        } else {
            if (canSend2) {
                canSend2 = false;
                send_holiday_or_comp_off(the_type, s_reason);
            }
        }
    }

    private void check_is_date_exists() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    if (response.equals(Constants.KEY_EXISTS)) {
                        switch (request_type) {
                            case Constants.KEY_1:
                            case Constants.KEY_2:
                            case Constants.KEY_3:
                                check_in_out_layout.setVisibility(View.GONE);
                                send_request.setEnabled(false);
                                send_request.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.signature_blue)));
                                break;
                            case Constants.KEY_10:
                                check_in_out_layout.setVisibility(View.GONE);
                                break;
                        }
                    } else if (response.equals(Constants.KEY_DOES_NOT_EXISTS)) {
                        if (request_type.equals(Constants.KEY_10)) {
                            check_in_out_layout.setVisibility(View.VISIBLE);
                        } else if (request_type.equals(Constants.KEY_3)) {
                            check_in_out_layout.setVisibility(View.GONE);
                        }
                        send_request.setEnabled(true);
                        send_request.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.nissi_blue)));
                    }
                },
                error -> supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_CHECK_IS_THE_REQUEST_EXIST);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_DATE, the_date);
                params.put(Constants.KEY_TYPE, request_type);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date_format_2(String date_2) {
        SimpleDateFormat format_1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format_2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format_1.parse(date_2);
            return format_2.format(date);
        } catch (Exception e) {
            return "00";
        }
    }

    private void sendNotification(String typeCheck) {
        if (typeCheck.equals(Constants.KEY_1)) {
            long timeNow = new Date().getTime();

            preferencesManager.putString(Constants.KEY_LAST_CHECKED_IN_TIME, String.valueOf(timeNow));
            preferencesManager.putBoolean(Constants.KEY_LAST_CHECKED_IN, true);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            long triggerTime = timeNow + 1 * 60 * 1000;
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else {
            preferencesManager.putBoolean(Constants.KEY_LAST_CHECKED_IN, false);
        }
    }

}
