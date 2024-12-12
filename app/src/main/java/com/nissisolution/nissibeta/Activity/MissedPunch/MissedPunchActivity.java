package com.nissisolution.nissibeta.Activity.MissedPunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.nissisolution.nissibeta.Adapter.Missed_Punch.MissedPunchAdapter;
import com.nissisolution.nissibeta.Classes.MissedPunch;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Filter;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissedPunchActivity extends AppCompatActivity {

    private TextView licence;
    private ArrayAdapter<String> month_adapter, year_adapter, type_adapter;
    private Spinner month_spinner, year_spinner, type_spinner;
    private RecyclerView recyclerView;
    private Supports supports;
    private PreferencesManager preferencesManager;
    private Context context;
    private String selected_year, selected_month, the_date;
    private List<String> year_array;
    private Button new_request, send_request, cancel;
    private Dialog dialog;
    private TextInputEditText missed_date, reason, start_time, end_time;
    private LinearLayout start_time_layout, end_time_layout;
    private ImageView date_calender, start_time_image, end_time_image;
    private String s_missed_date, s_working_hours, s_reason, s_start_time, s_end_time;
    private List<MissedPunch> missed_punch_list;
    private MissedPunchAdapter missed_punch_adapter;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private Double original_duration, final_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_punch);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        set_toolbar();
        declare_item();
        set_listeners();
        set_spinners();
        set_initial_data();
        updateViewed();
    }

    private void updateViewed() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                }, error -> {
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_UPDATE_VIEWED);
                map.put(Constants.KEY_TYPE, Constants.KEY_10);
                map.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void declare_item() {
        licence = findViewById(R.id.licence);
        context = this;
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(this);
        recyclerView = findViewById(R.id.recyclerView);
        month_spinner = findViewById(R.id.month);
        year_spinner = findViewById(R.id.year);
        new_request = findViewById(R.id.new_request);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_missed_punch);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        missed_date = dialog.findViewById(R.id.missed_date);
        start_time_layout = dialog.findViewById(R.id.start_time_layout);
        end_time_layout = dialog.findViewById(R.id.end_time_layout);
        start_time = dialog.findViewById(R.id.start_time);
        end_time = dialog.findViewById(R.id.end_time);
        start_time_image = dialog.findViewById(R.id.start_time_image);
        end_time_image = dialog.findViewById(R.id.end_time_image);
        reason = dialog.findViewById(R.id.reason);
        send_request = dialog.findViewById(R.id.send_request);
        cancel = dialog.findViewById(R.id.cancel);
        date_calender = dialog.findViewById(R.id.date_calender);
        year_array = new ArrayList<>();
        missed_punch_list = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        type_spinner = findViewById(R.id.type);
    }

    private void set_listeners() {
        supports.licence(licence);

        date_calender.setOnClickListener(view -> open_calender());

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (i < 10) {
                        selected_month = "0" + i;
                    } else {
                        selected_month = String.valueOf(i);
                    }
                } else {
                    selected_month = "null";
                }
                filter_data();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selected_year = String.valueOf(year_spinner.getSelectedItem());
                } else {
                    selected_year = "null";
                }
                filter_data();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set_recycler_view();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new_request.setOnClickListener(view -> {
            dialog.setCancelable(false);
            dialog.show();
        });

        cancel.setOnClickListener(view -> dialog.dismiss());

        send_request.setOnClickListener(view -> check_condition());

        missed_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (check_date(missed_date.getText().toString().trim())) {
                    check_is_date_exists();
                    missed_date.setError(null);
                } else {
                    missed_date.setError(Constants.KEY_INVALID_FORMAT);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        time_changer(start_time);
        time_changer(end_time);
        text_cl(start_time, start_time_image);
        text_cl(end_time, end_time_image);
    }

    private void text_cl(TextInputEditText element_1, ImageView element_2) {
        element_2.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    element_1.setText(get_time(selectedHour, selectedMinute));
                }
            }, hour, minute, false);
            mTimePicker.show();
        });
    }

    @SuppressLint("SimpleDateFormat")
    private String get_time(int hour, int minute)
    {
        SimpleDateFormat format_1 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format_2 = new SimpleDateFormat("hh:mm a");
        try {
            return format_2.format(format_1.parse(hour + ":" + minute));
        } catch (Exception ignored) {
            return null;
        }
    }

    private void time_changer(TextInputEditText element) {
        element.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (element.getText().toString().trim().isEmpty()) {
                    element.setError(Constants.KEY_REQUIRED);
                } else if (get_timestamp(element.getText().toString().trim()) == 0) {
                    element.setError(Constants.KEY_INVALID_FORMAT);
                } else {
                    element.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void set_spinners() {
        year_array.add(Constants.KEY_SELECT_YEAR);
        for (int i = get_year(); i > 2022; i--) {
            year_array.add(String.valueOf(i));
        }
        year_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, year_array);
        year_spinner.setAdapter(year_adapter);
        month_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, getResources().getStringArray(R.array.month_array));
        month_spinner.setAdapter(month_adapter);
        type_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, getResources().getStringArray(R.array.type_array));
        type_spinner.setAdapter(type_adapter);
    }

    private void set_toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.missed_punch));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void filter_data() {
        if (selected_month.equals(Constants.KEY_NULL) || selected_year.equals(Constants.KEY_NULL)) {
            missed_punch_list.clear();
            set_recycler_view();
        } else {
            get_request_data();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private int get_year() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return Integer.parseInt(format.format(new Date()));
    }

    @SuppressLint("SetTextI18n")
    private void open_calender() {
        Calendar c =  Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String d, m, y;
                if (day<10) {
                    d = "0" + day;
                } else {
                    d = String.valueOf(day);
                }

                if (month < 9) {
                    m = "0" + (month + 1);
                } else {
                    m = String.valueOf(month+1);
                }

                y = String.valueOf(year);

                missed_date.setText(d + "-" + m + "-" + y);
            }
        },year, month, day);

        pickerDialog.getDatePicker().setMinDate(supports.get_timestamp(year, month+1, 1));
        pickerDialog.getDatePicker().setMaxDate(new Date().getTime());

        pickerDialog.show();

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

    private void set_error(TextInputEditText element, String message) {
        element.setError(message);
        element.requestFocus();
    }

    private void check_condition() {
        s_missed_date = missed_date.getText().toString().trim();
        s_start_time = start_time.getText().toString().trim();
        s_end_time = end_time.getText().toString().trim();
        double duration = get_duration(s_start_time, s_end_time);
        s_working_hours = String.valueOf(duration);
        s_reason = reason.getText().toString().trim();
        the_date = get_date_format_2(s_missed_date);

        if (s_missed_date.isEmpty()) {
            set_error(missed_date, Constants.KEY_REQUIRED);
        } else if (!check_date(s_missed_date)) {
            set_error(missed_date, Constants.KEY_INVALID_FORMAT);
        } else if (s_start_time.isEmpty()) {
            set_error(start_time, Constants.KEY_REQUIRED);
        } else if (get_timestamp(s_start_time) == 0) {
            set_error(start_time, Constants.KEY_INVALID_FORMAT);
        } else if (s_end_time.isEmpty()) {
            set_error(end_time, Constants.KEY_REQUIRED);
        } else if (get_timestamp(s_end_time) == 0) {
            set_error(end_time, Constants.KEY_INVALID_FORMAT);
        } else if (duration == 0) {
            set_error(end_time, Constants.KEY_INVALID_FORMAT);
        } else if (s_reason.isEmpty()) {
            set_error(reason, Constants.KEY_REQUIRED);
        } else {
            final_duration = original_duration + duration;
            s_working_hours = String.valueOf(final_duration);
            set_request_data();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private long get_timestamp(String the_time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            return format.parse(the_time).getTime();
        } catch (Exception ignored) {
            return 0;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private double get_duration(String start_t, String end_t) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            long timestamp_1 = format.parse(start_t).getTime();
            long timestamp_2 = format.parse(end_t).getTime();
            if (timestamp_2 > timestamp_1) {
                Long dur = timestamp_2 - timestamp_1;
                double du = dur.doubleValue();
                return du / (60 * 60 * 1000);
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean check_date(String the_date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        long dt;
        try {
            dt =  format.parse(the_date).getTime();
        } catch (Exception exception) {
            return false;
        }

        Calendar c =  Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        return supports.get_timestamp(year, month + 1, 1) <= dt && new Date().getTime() > dt;

    }

    private void get_request_data() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_request_data, error -> {
            supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
            progressBar.setVisibility(View.GONE);
        })
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_MISSED_PUNCH_DATA);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_MONTH, selected_month);
                params.put(Constants.KEY_YEAR, selected_year);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_request_data(String the_response) {
        progressBar.setVisibility(View.GONE);
        missed_punch_list.clear();
        List<String> list_1 = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_3));
        for (int i = 1; i < list_1.size(); i++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_4));
            MissedPunch missed_punch = new MissedPunch(list_2.get(0), "From " + list_2.get(4) + " to " + list_2.get(5),
                    supports.get_current_status(list_2.get(2)), list_1.get(i), Integer.parseInt(list_2.get(6)), Long.parseLong(list_2.get(3)));
            missed_punch_list.add(missed_punch);
        }
        //sort_array();
        set_recycler_view();
    }

    private void sort_array() {
        List<MissedPunch> temp_arr = new ArrayList<>();
        List<Integer> temp_list = new ArrayList<>();
        for (MissedPunch punch:missed_punch_list) {
            temp_list.add(punch.id);
        }
        List<Integer> temp_list_2 = temp_list;
        Collections.sort(temp_list_2);
//        for (:
//             ) {
//
//        }
    }

    private void set_recycler_view() {
//        Missed_Punch[] arr = missed_punch_list.toArray(new Missed_Punch[0]);
//        Arrays.sort(arr, new SortByTimestamp());
//        missed_punch_list = Arrays.asList(arr);
        missed_punch_adapter = new MissedPunchAdapter(context, Filter.filterMissedPunch(missed_punch_list, type_spinner.getSelectedItemPosition()), progressBar);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(missed_punch_adapter);
    }

    private void set_request_data() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    if (response.equals(Constants.KEY_SUCCESS)) {
                        get_request_data();
                        set_enabled(true);
                        clear_dialog();
                    } else if (response.equals(Constants.KEY_FAILURE)) {
                        supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    supports.create_short_toast(error.getMessage());
                    progressBar.setVisibility(View.GONE);
                })
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_WORK_HOURS, s_working_hours);
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_HOLIDAY_OR_COMP_OFF);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_FROM_DATE, the_date);
                params.put(Constants.KEY_REASON, s_reason);
                params.put(Constants.KEY_TYPE, Constants.KEY_10);
                params.put(Constants.KEY_FROM_TIME, s_start_time);
                params.put(Constants.KEY_TO_TIME, s_end_time);
                params.put(Constants.KEY_TIMESTAMP, String.valueOf(new Date().getTime()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void set_initial_data() {
        Calendar cal = Calendar.getInstance();
        int mon = cal.get(Calendar.MONTH) + 1;
        month_spinner.setSelection(mon);
        year_spinner.setSelection(1);
        if (mon < 10) {
            selected_month = "0" + mon;
        } else {
            selected_month = String.valueOf(mon);
        }
        selected_year = String.valueOf(cal.get(Calendar.YEAR));
        get_request_data();
    }

    private void check_is_date_exists() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    if (response.equals(Constants.KEY_EXISTS)) {
                        progressBar.setVisibility(View.GONE);
                        send_request.setEnabled(false);
                        send_request.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.signature_blue)));
                    } else if (response.equals(Constants.KEY_DOES_NOT_EXISTS)) {
                        send_request.setEnabled(true);
                        send_request.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.nissi_blue)));
                        get_check_in_out_data();
                    }
                },
                error -> {
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    progressBar.setVisibility(View.GONE);
                })
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_CHECK_IS_THE_REQUEST_EXIST);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_DATE, get_date_format_2(missed_date.getText().toString().trim()));
                params.put(Constants.KEY_TYPE, "10");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void get_check_in_out_data() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_check_in_data,
                error -> {
                    progressBar.setVisibility(View.GONE);
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_CHECK_IN_OUT_DATA);
                params.put(Constants.KEY_DATE, get_date_format_2(missed_date.getText().toString().trim()));
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_check_in_data (String the_response) {
        original_duration = supports.get_last_checked_time_value(the_response);
        set_start_time(the_response);
    }

    private void set_start_time (String the_response) {
        String the_value = supports.get_last_checked_time_text(the_response);
        start_time.setText(the_value);
        start_time.setError(null);
        set_enabled(the_value == null);
        progressBar.setVisibility(View.GONE);
    }

    private void set_enabled(boolean the_condition) {
        start_time.setEnabled(the_condition);
        start_time_image.setEnabled(the_condition);
        start_time_layout.setEnabled(the_condition);
    }

    private void clear_dialog() {
        dialog.hide();
        missed_date.setText(null);
        missed_date.setError(null);
        start_time.setText(null);
        start_time.setError(null);
        end_time.setText(null);
        end_time.setError(null);
        reason.setText(null);
    }

}
