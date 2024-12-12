package com.nissisolution.nissibeta.Activity.Locations;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Adapter.PunchedLocation.PunchedLocationAdapter;
import com.nissisolution.nissibeta.Classes.CheckedInfo;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminLocationActivity extends AppCompatActivity {

    private Spinner staff_name;
    private RadioGroup radio;
    private TextView date;
    private Button pick_date, filter;
    private ProgressBar progressBar;
    private List<String> staff_list;
    private List<Integer> staff_id_list;
    private PreferencesManager preferencesManager;
    private Supports supports;
    private Context context;
    private ArrayAdapter<String> staff_adapter;
    private String type;
    private List<CheckedInfo> checked_list;
    private RecyclerView recyclerView;
    private PunchedLocationAdapter punched_location_adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location);
        declare_item();
        set_listeners();
        get_direct_employee();
        set_toolbar();
    }

    private void declare_item() {
        staff_name = findViewById(R.id.staff_name);
        radio = findViewById(R.id.radio);
        date = findViewById(R.id.date);
        date.setText(get_date());
        progressBar = findViewById(R.id.progressBar);
        staff_list = new ArrayList<>();
        staff_id_list = new ArrayList<>();
        context = this;
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(context);
        type = Constants.KEY_0;
        pick_date = findViewById(R.id.pick_date);
        filter = findViewById(R.id.filter);
        checked_list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void set_listeners() {
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.punched_in:
                        type = Constants.KEY_1;
                        break;
                    case R.id.punched_out:
                        type = Constants.KEY_2;
                        break;
                    default:
                        type = Constants.KEY_0;
                        break;
                }
            }
        });
        filter.setOnClickListener(v -> get_location_info());
        pick_date.setOnClickListener(v -> open_calender());
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

                date.setText(d + "-" + m + "-" + y);
            }
        },year, month, day);

        // pickerDialog.getDatePicker().setMinDate(supports.get_timestamp(year, month+1, 1));
        pickerDialog.getDatePicker().setMaxDate(new Date().getTime());

        pickerDialog.show();

    }

    private void get_direct_employee() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_response_1, error -> {
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    progressBar.setVisibility(View.GONE);
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_DIRECT_EMPLOYEE);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
//                params.put(Constants.KEY_STAFF_ID, "38");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_response_1(String the_response) {
        progressBar.setVisibility(View.GONE);
        staff_list.clear();
        staff_id_list.clear();
        staff_list.add("All Staffs");
        staff_id_list.add(0);
        List<String> list_1 = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_1));
        for (int i = 1; i < list_1.size(); i++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
            staff_id_list.add(Integer.parseInt(list_2.get(0)));
            staff_list.add(list_2.get(1));
        }
        set_spinner();
        get_location_info();
    }

    private void set_spinner() {
        staff_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout_2, staff_list);
        staff_name.setAdapter(staff_adapter);
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(new Date());
    }

    private void get_location_info() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_response_2, error -> {
            progressBar.setVisibility(View.GONE);
            supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_DIRECT_EMPLOYEE_INFO);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
//                params.put(Constants.KEY_STAFF_ID, "270");
                params.put(Constants.KEY_DATE, get_date_2());
                params.put(Constants.KEY_REASON, String.valueOf(staff_id_list.get(staff_name.getSelectedItemPosition())));
                params.put(Constants.KEY_TYPE, type);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_response_2(String the_response) {
        progressBar.setVisibility(View.GONE);
        checked_list.clear();
        List<String> list1 = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_1));

        if (list1.size() == 1) {
            supports.create_short_toast("No data found");
        }

        for (int i = 1; i < list1.size(); i++) {
            List<String> list2 = Arrays.asList(list1.get(i).split(Constants.KEY_SPLITTER_2));
            CheckedInfo info = new CheckedInfo(list2.get(0), Double.parseDouble(list2.get(1).trim()), Double.parseDouble(list2.get(2).trim()), list2.get(3), Integer.parseInt(list2.get(4)), list1.get(i));
            checked_list.add(info);
        }
        set_recycler();
    }

    private void set_recycler() {
        punched_location_adapter = new PunchedLocationAdapter(context, checked_list);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(punched_location_adapter);
    }

    private void set_toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.punched_info));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date_2() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return format1.format(format2.parse(date.getText().toString().trim()));
        } catch (Exception ignored) {
            return  "O";
        }
    }

}