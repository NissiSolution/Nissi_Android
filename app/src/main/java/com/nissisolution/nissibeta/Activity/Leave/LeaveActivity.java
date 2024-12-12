package com.nissisolution.nissibeta.Activity.Leave;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.nissisolution.nissibeta.Adapter.Leave.LeaveAdapter;
import com.nissisolution.nissibeta.Adapter.Text.TextAdapter;
import com.nissisolution.nissibeta.Classes.Leave;
import com.nissisolution.nissibeta.Classes.LeaveBalance;
import com.nissisolution.nissibeta.Classes.LeaveData;
import com.nissisolution.nissibeta.Classes.LeaveList;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Filter;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveActivity extends AppCompatActivity {

    private Context context;
    private PreferencesManager preferencesManager;
    private Supports supports;
    private Button apply_for_leave, sendRequest, cancel, d_send_request, d_cancel;
    private ArrayAdapter<String> month_adapter, year_adapter, type_adapter;
    private TextAdapter textAdapter;
    private List<String> textList;
    private Spinner month_spinner, year_spinner, type_spinner;
    private String selected_year, selected_month, the_date;
    private RecyclerView recyclerView, remainingLeaveRecyclerview;
    private double i_remaining_leave, i_total_leave;
    private TextView licence, numberOfDays, remaining_leave, total_leave;
    private Dialog leaveDialog, error_dialog;
    private Spinner leave_type;
    private TextInputEditText purpose, fDate, tDate, reason;
    private ImageView f_calender, t_calender;
    private String sFDate, sTDate, s_purpose, s_leave_type, sNumberOfDays, s_reason;
    private Boolean isActive;
    private List<LeaveData> leave_data_list;
    private ArrayAdapter<String> leave_list_adapter;
    private List<String> leave_list, year_array, symbolList;
    private List<LeaveList> leave_list_list;
    private ProgressBar progressBar;
    private boolean is_date_exist = false;
    private LeaveAdapter leave_adapter;
    private List<Leave> leaveInfoList;
    private LinearLayoutManager layoutManager, layoutManager2;
    private CheckBox firstHalf, secondHalf;
    private List<LeaveBalance> leaveBalanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        declareItem();
        setListeners();
        setToolbar();
        setSpinners();
        checkIsDateExists();
        checkDateCondition();
        setInitialData();
        updateViewed();
    }

    private void declareItem() {
        context = LeaveActivity.this;
        preferencesManager = new  PreferencesManager(context);
        supports = new Supports(context);
        apply_for_leave = findViewById(R.id.apply_for_leave);
        recyclerView = findViewById(R.id.recyclerView);
        licence = findViewById(R.id.licence);
        leaveDialog = new Dialog(context);
        leaveDialog.setContentView(R.layout.dialog_leave);
        leaveDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leaveDialog.setCancelable(false);
        purpose = leaveDialog.findViewById(R.id.purpose);
        leave_type = leaveDialog.findViewById(R.id.leave_type);
        fDate = leaveDialog.findViewById(R.id.f_date);
        f_calender = leaveDialog.findViewById(R.id.f_calender);
        tDate = leaveDialog.findViewById(R.id.t_date);
        t_calender = leaveDialog.findViewById(R.id.t_calender);
        sendRequest = leaveDialog.findViewById(R.id.send_request);
        cancel = leaveDialog.findViewById(R.id.cancel);
        numberOfDays = leaveDialog.findViewById(R.id.number_of_days);
        firstHalf = leaveDialog.findViewById(R.id.firstHalf);
        secondHalf = leaveDialog.findViewById(R.id.secondHalf);
        progressBar = findViewById(R.id.progressBar);
        month_spinner = findViewById(R.id.month);
        year_spinner = findViewById(R.id.year);
        reason = leaveDialog.findViewById(R.id.reason);
        remaining_leave = leaveDialog.findViewById(R.id.remaining_days);
        total_leave = leaveDialog.findViewById(R.id.total_leave);
        remainingLeaveRecyclerview = findViewById(R.id.remaining_leave);
        leave_data_list = new ArrayList<>();
        leave_list = new ArrayList<>();
        leave_list_list = new ArrayList<>();
        leaveInfoList = new ArrayList<>();
        year_array = new ArrayList<>();
        textList = new ArrayList<>();
        symbolList = new ArrayList<>();
        error_dialog = new Dialog(context);
        error_dialog.setContentView(R.layout.dialog_lop_error);
        error_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        error_dialog.setCancelable(false);
        d_cancel = error_dialog.findViewById(R.id.cancel);
        d_send_request = error_dialog.findViewById(R.id.send_request);
        type_spinner = findViewById(R.id.type);
        leaveBalanceList = new ArrayList<>();
    }

    private void setListeners() {
        apply_for_leave.setOnClickListener(view -> leaveDialog.show());
        f_calender.setOnClickListener(view -> open_calender(1));
        t_calender.setOnClickListener(view -> open_calender(2));
        fDate.setEnabled(false);
        tDate.setEnabled(false);
        firstHalf.setOnCheckedChangeListener((buttonView, isChecked) -> calculateDates());
        secondHalf.setOnCheckedChangeListener(((buttonView, isChecked) -> calculateDates()));
        cancel.setOnClickListener(v -> leaveDialog.hide());
        sendRequest.setOnClickListener(view -> checkCondition());
        supports.licence(licence);
        fDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkDateCondition();
                fDate.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkDateCondition();
                tDate.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    i_remaining_leave = 0;
                    i_total_leave = 0;
                    progressBar.setVisibility(View.VISIBLE);

                    i_total_leave = 0;
                    i_remaining_leave = 0;
                    total_leave.setText(Constants.KEY_0);
                    remaining_leave.setText(Constants.KEY_0);

                    for (LeaveBalance leaveBalance : leaveBalanceList) {
                        if (symbolList.get(leave_type.getSelectedItemPosition()).equals(leaveBalance.slug)) {
                            i_total_leave = Double.parseDouble(leaveBalance.total);
                            i_remaining_leave = Double.parseDouble(leaveBalance.remain);
                            total_leave.setText(leaveBalance.total);
                            remaining_leave.setText(leaveBalance.remain);
                            break;
                        } else if (symbolList.get(leave_type.getSelectedItemPosition()).equals("SI") && leaveBalance.slug.equals("SL")) {
                            i_total_leave = Double.parseDouble(leaveBalance.total);
                            i_remaining_leave = Double.parseDouble(leaveBalance.remain);
                            total_leave.setText(leaveBalance.total);
                            remaining_leave.setText(leaveBalance.remain);
                            break;
                        }
                    }
//                    get_balance_leave();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                setRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        supports.edit_text_watcher(purpose);
        supports.edit_text_watcher(reason);
        d_cancel.setOnClickListener(v -> error_dialog.dismiss());
        d_send_request.setOnClickListener(v -> {
            sendLeaveRequest();
            error_dialog.hide();
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.leave_information));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void setSpinners() {
        year_array.add(Constants.KEY_SELECT_YEAR);
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i > 2022; i--) {
            year_array.add(String.valueOf(i));
        }
        year_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, year_array);
        year_spinner.setAdapter(year_adapter);
        month_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, getResources().getStringArray(R.array.month_array));
        month_spinner.setAdapter(month_adapter);
        type_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, getResources().getStringArray(R.array.type_array));
        type_spinner.setAdapter(type_adapter);
    }

    private void filter_data() {
        getRequestData();
    }

    @SuppressLint("SetTextI18n")
    private void open_calender(int type) {
        Calendar c =  Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        long firstTimestamp = getPreviousMonthFirstDate(month, year);
        DatePickerDialog pickerDialog = new DatePickerDialog(context, (datePicker, year1, month1, day1) -> {
            String d, m, y;
            if (day1 <10) {
                d = "0" + day1;
            } else {
                d = String.valueOf(day1);
            }

            if (month1 < 9) {
                m = "0" + (month1 + 1);
            } else {
                m = String.valueOf(month1 +1);
            }

            y = String.valueOf(year1);

            if (type == 1) {
                fDate.setText(d + "-" + m + "-" + y);
            } else {
                tDate.setText(d + "-" + m + "-" + y);
            }
            calculateDates();
            checkDateCondition();
        },year, month, day);

        pickerDialog.getDatePicker().setMinDate(firstTimestamp);

        pickerDialog.show();

    }

    private long getPreviousMonthFirstDate(int month, int year) {
        if (month == 0) {
            month = 11;
            year = year - 1;
        } else {
            month = month - 1;
        }

        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        Date date = c.getTime();
        return date.getTime();
    }

    private void calculateDates() {
        sFDate = fDate.getText().toString().trim();
        sTDate = tDate.getText().toString().trim();
        sNumberOfDays = "";
        try {
            if (sFDate.equals(sTDate) || sFDate.isEmpty() || sTDate.isEmpty()) {
                secondHalf.setEnabled(false);
                secondHalf.setChecked(false);
            } else {
                secondHalf.setEnabled(true);
            }
            List<String> f_d = Arrays.asList(sFDate.split("-"));
            List<String> t_d = Arrays.asList(sTDate.split("-"));
            int fd = Integer.parseInt(f_d.get(0));
            int fm = Integer.parseInt(f_d.get(1));
            int fy = Integer.parseInt(f_d.get(2));

            int td = Integer.parseInt(t_d.get(0));
            int tm = Integer.parseInt(t_d.get(1));
            int ty = Integer.parseInt(t_d.get(2));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate start = LocalDate.of(fy, fm, fd);
                LocalDate end = LocalDate.of(ty, tm, td);
                Period period = Period.between(start, end);
                double days = period.getDays();

                if (firstHalf.isChecked()) {
                    days = days - 0.5;
                }
                if (secondHalf.isChecked()) {
                    days = days - 0.5;
                }

                sNumberOfDays = String.valueOf(days + 1);
                List<String> list1 = Arrays.asList(sNumberOfDays.split("\\."));
                if (list1.get(1).equals(Constants.KEY_0)) {
                    sNumberOfDays = list1.get(0);
                }
                numberOfDays.setText(sNumberOfDays);
                checkIsDateExists();
            }
        } catch (Exception e) {
            numberOfDays.setText("Invalid Format");
        }

    }

    private void checkCondition() {
        s_purpose = purpose.getText().toString().trim();
        s_leave_type = leave_type.getSelectedItem().toString();
        s_reason = reason.getText().toString().trim();
        if (leave_type.getSelectedItemPosition() == 0) {
            supports.create_short_toast("Leave type required");
        } else if (s_purpose.isEmpty()) {
            set_error(purpose, "Purpose required");
        } else if (!is_date_format(sFDate)) {
            set_error(fDate, "Invalid date");
        } else if (!is_date_format(sTDate)) {
            set_error(tDate, "Invalid date");
        } else if (sNumberOfDays == null || sNumberOfDays.isEmpty() || Double.parseDouble(sNumberOfDays) <= 0) {
            supports.create_short_toast("Invalid number of days");
        } else if (s_reason.isEmpty()) {
            set_error(reason, "Reason Required");
        } else if (Double.parseDouble(sNumberOfDays) > i_remaining_leave) {
            error_dialog.show();
        } else {
            sendLeaveRequest();
        }
    }

    private void set_error(TextInputEditText element, String message) {
        element.setError(message);
        element.requestFocus();
    }

    @SuppressLint("SimpleDateFormat")
    private boolean is_date_format(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date_s = format.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private void get_leave_type() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_leave_response, error -> {
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    progressBar.setVisibility(View.GONE);
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_LEAVE_TYPE);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void sendLeaveRequest() {
        if (isActive) {
            String d_1 = get_date_format_3(sFDate);
            String d_2 = get_date_format_3(sTDate);
            progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK, response -> {
                progressBar.setVisibility(View.GONE);
                if (response.equals(Constants.KEY_SUCCESS)) {
                    supports.create_short_toast(Constants.KEY_REQUEST_SEND_SUCCESSFULLY);
                    leaveDialog.hide();
                    purpose.setText(null);
                    fDate.setText(null);
                    tDate.setText(null);
                    reason.setText(null);
                    numberOfDays.setText(Constants.KEY_0);
                    leave_type.setSelection(0);
                    total_leave.setText(Constants.KEY_0);
                    remaining_leave.setText(Constants.KEY_0);
                    getRequestData();
                } else if (response.equals(Constants.KEY_FAILURE)){
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                } else {
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                }
//                supports.alert_dialog(response);
            }, error -> {
                progressBar.setVisibility(View.GONE);
                supports.create_short_toast(error.toString());
            }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_SEND_LEAVE_REQUEST);
                    params.put(Constants.KEY_PURPOSE, s_purpose);
                    params.put(Constants.KEY_FROM_DATE, d_1);
                    params.put(Constants.KEY_TO_DATE, d_2);
                    params.put(Constants.KEY_REASON, s_reason);
                    if (leave_type.getSelectedItemPosition() == 1) {
                        params.put(Constants.KEY_TYPE, Constants.KEY_1);
                    } else {
                        params.put(Constants.KEY_TYPE, Constants.KEY_0);
                    }
                    params.put(Constants.KEY_DATE_CREATED, get_created_date());
                    params.put(Constants.KEY_NUMBER_OF_DAYS, String.valueOf(i_remaining_leave));
                    params.put(Constants.KEY_NUMBER_OF_LEAVING_DAY, sNumberOfDays);
                    params.put(Constants.KEY_TYPE_OF_LEAVE_TEXT, leave_list_list.get(leave_type.getSelectedItemPosition()-1).slug);
                    params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                    params.put(Constants.KEY_TIMESTAMP, String.valueOf(new Date().getTime()));
                    params.put(Constants.KEY_SYMBOL, leave_list_list.get(leave_type.getSelectedItemPosition()-1).symbol);
                    if (firstHalf.isChecked() && secondHalf.isChecked()) {
                        params.put(Constants.KEY_FROM_TIME, Constants.KEY_3);
                    } else if (secondHalf.isChecked()) {
                        params.put(Constants.KEY_FROM_TIME, Constants.KEY_2);
                    } else if (firstHalf.isChecked()) {
                        params.put(Constants.KEY_FROM_TIME, Constants.KEY_1);
                    } else {
                        params.put(Constants.KEY_FROM_TIME, Constants.KEY_0);
                    }

                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date_format_3 (String c_date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");
        try {
            Date date = format.parse(c_date);
            return format1.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String get_created_date() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");
        return format.format(new Date().getTime());
    }

    private void get_leave_requests() {
        if (isActive) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handle_response, error -> {
                        supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    })
            {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_MY_ATTENDANCE_REQUEST);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }

    private void handle_response(String the_response) {
        List<String> first_list = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_1));
        for (int i = 1; i < first_list.size(); i++) {
            try {
                List<String> second_list = Arrays.asList(first_list.get(i).split(Constants.KEY_SPLITTER_2));
                LeaveData leave_data = new LeaveData(second_list.get(0), second_list.get(1),
                        second_list.get(2), second_list.get(3), second_list.get(4), Double.parseDouble(second_list.get(5)),
                        Double.parseDouble(second_list.get(6)), Double.parseDouble(second_list.get(7)), second_list.get(8));
                leave_data_list.add(leave_data);
            } catch (Exception ignored) {

            }
        }
    }

    private void handle_leave_response(String the_response) {
        List<String> first_list = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_1));
        leave_list.clear();
        leave_list_list.clear();
        symbolList.clear();
        leave_list.add(Constants.KEY_SELECT);
        symbolList.add(Constants.KEY_SELECT);
        for (int i = 1; i < first_list.size(); i++) {
            List<String> second_list = Arrays.asList(first_list.get(i).split(Constants.KEY_SPLITTER_2));
            LeaveList current_list = new LeaveList(second_list.get(0), second_list.get(1), second_list.get(2));
            leave_list.add(second_list.get(0));
            symbolList.add(second_list.get(2));
            leave_list_list.add(current_list);
            set_leave_adapter();
        }

        set_leave_adapter();
        get_all_leave_data();

    }

    private void get_all_leave_data() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    handle_all_leave_response(response);
                }, error -> {
                    progressBar.setVisibility(View.GONE);
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_ALL_LEAVE_BALANCE);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_DATE, String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_all_leave_response(String the_response) {
        // supports.alert_dialog(the_response);
        List<String> list_1 = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_1));
        textList.clear();
        for (int i = 1; i < list_1.size(); i++) {
            textList.add(list_1.get(i));
        }
        leaveBalanceList.clear();
        for (String text : textList) {
            List<String> list2 = Arrays.asList(text.split(Constants.KEY_SPLITTER_2));
            leaveBalanceList.add(new LeaveBalance(list2.get(0), list2.get(1), list2.get(2)));
        }
        setRemainingLeaveAdapter();
        getRequestData();
    }

    private void setRemainingLeaveAdapter() {
        textAdapter = new TextAdapter(context, textList);
        layoutManager2 = new LinearLayoutManager(context);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        remainingLeaveRecyclerview.setHasFixedSize(true);
        remainingLeaveRecyclerview.setLayoutManager(layoutManager2);
        remainingLeaveRecyclerview.setAdapter(textAdapter);
    }

    private void set_leave_adapter() {
        leave_list_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, leave_list);
        leave_type.setAdapter(leave_list_adapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
        get_leave_requests();
        get_leave_type();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    private void checkIsDateExists() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    if (response.equals(Constants.KEY_EXISTS)) {
                        is_date_exist = true;
                    } else if (response.equals(Constants.KEY_DOES_NOT_EXISTS)) {
                        is_date_exist = false;
                    }
                    checkDateCondition();
                },
                error -> supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_CHECK_IS_THE_REQUEST_EXIST);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_DATE, get_date_format_2());
                params.put(Constants.KEY_TYPE, Constants.KEY_0);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date_format_1() {
        SimpleDateFormat format_2 = new SimpleDateFormat("dd-MM-yyyy");
        return format_2.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date_format_2() {
        SimpleDateFormat format_2 = new SimpleDateFormat("yyyy-MM-dd");
        return format_2.format(new Date());
    }

    private void checkDateCondition() {
        if (is_date_exist) {
            if (fDate.getText().toString().equals(get_date_format_1())) {
                sendRequest.setEnabled(false);
                sendRequest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.signature_blue)));
            } else {
                sendRequest.setEnabled(true);
                sendRequest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.nissi_blue)));
            }
        } else {
            sendRequest.setEnabled(true);
            sendRequest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.nissi_blue)));
        }

    }

    private void get_balance_leave() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    if (response.equals(Constants.KEY_FAILURE)){
                        i_total_leave = 0;
                        i_remaining_leave = 0;
                        total_leave.setText(Constants.KEY_0);
                        remaining_leave.setText(Constants.KEY_0);
                    } else {
                        List<String> list_1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_2));
                        i_total_leave = Double.parseDouble(list_1.get(0));
                        i_remaining_leave = Double.parseDouble(list_1.get(1));
                        total_leave.setText(String.valueOf(i_total_leave));
                        remaining_leave.setText(String.valueOf(i_remaining_leave));
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    progressBar.setVisibility(View.GONE);
                })
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_BALANCE_LEAVE);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_YEAR, String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                params.put(Constants.KEY_TYPE, leave_list_list.get(leave_type.getSelectedItemPosition()-1).slug);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void setInitialData() {
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
    }

    private void getRequestData() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_request_data, error -> supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_LEAVE_REQUEST_DATA);
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
        leaveInfoList.clear();
        List<String> list_1 = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_3));
        for (int i = 1; i < list_1.size(); i++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_4));
            String the_type = get_leave_type(list_2.get(5));

            Leave the_leave = new Leave(list_2.get(0), list_2.get(1), the_type, supports.get_current_status(list_2.get(3)),
                    Double.parseDouble(list_2.get(8)), list_1.get(i), Integer.parseInt(list_2.get(9)), Long.parseLong(list_2.get(4)));
            leaveInfoList.add(the_leave);
        }

        setRecyclerView();
    }

    private String get_leave_type(String the_type) {
        for (int i = 0; i < leave_list_list.size(); i++) {
            if (leave_list_list.get(i).slug.equals(the_type)) {
                return leave_list_list.get(i).type_name;
            }
        }
        return "Unknown";
    }

    private void setRecyclerView() {
        leave_adapter = new LeaveAdapter(context, Filter.filterLeave(leaveInfoList, type_spinner.getSelectedItemPosition()), progressBar);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(leave_adapter);
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
                map.put(Constants.KEY_TYPE, Constants.KEY_0);
                map.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

}
