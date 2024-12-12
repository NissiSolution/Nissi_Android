package com.nissisolution.nissibeta.Activity.NightShift;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Adapter.AdminCompOffHoliday.AdminCompOffHolidayAdapter;
import com.nissisolution.nissibeta.Classes.AdminCompOffHoliday;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Filter;
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

public class AdminNightShiftActivity extends AppCompatActivity {

    private TextView licence;
    private ArrayAdapter<String> month_adapter, year_adapter, type_adapter;
    private Spinner month_spinner, year_spinner, type_spinner;
    private RecyclerView recyclerView;
    private Supports supports;
    private PreferencesManager preferencesManager;
    private Context context;
    private String selected_year, selected_month;
    private List<String> year_array;
    private List<AdminCompOffHoliday> admin_comp_off_holiday_list;
    private AdminCompOffHolidayAdapter admin_comp_off_holiday_adapter;
    private LinearLayoutManager layoutManager;
    private boolean isChecked = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_off_admin);

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
                map.put(Constants.KEY_TYPE, Constants.KEY_103);
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
        year_array = new ArrayList<>();
        admin_comp_off_holiday_list = new ArrayList<>();
        type_spinner = findViewById(R.id.type);
    }

    private void set_listeners() {
        supports.licence(licence);

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (i < 10) {
                        selected_month = Constants.KEY_0 + i;
                    } else {
                        selected_month = String.valueOf(i);
                    }
                } else {
                    selected_month = Constants.KEY_NULL;
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
                    selected_year = Constants.KEY_NULL;
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
    }

    private void set_spinners() {
        year_array.add(Constants.KEY_SELECT_YEAR);
        for (int i = get_year(); i > 2022; i--) {
            year_array.add(String.valueOf(i));
        }
        month_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, getResources().getStringArray(R.array.month_array));
        month_spinner.setAdapter(month_adapter);
        year_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, year_array);
        year_spinner.setAdapter(year_adapter);
        type_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, getResources().getStringArray(R.array.type_array));
        type_spinner.setAdapter(type_adapter);
    }

    private void set_toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.night_shift_request));
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
            admin_comp_off_holiday_list.clear();
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

    private void get_request_data() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_request_data, error -> supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_NIGHT_SHIFT_APPROVAL_REQUEST);
                //todo change staff id
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
//                params.put(Constants.KEY_STAFF_ID, "2");
                params.put(Constants.KEY_MONTH, selected_month);
                params.put(Constants.KEY_YEAR, selected_year);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_request_data(String the_response) {
        admin_comp_off_holiday_list.clear();
        List<String> list_1 = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_3));
        for (int i = 1; i < list_1.size(); i++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_4));
            AdminCompOffHoliday admin_comp_off_holiday = new AdminCompOffHoliday(list_2.get(0), list_2.get(5), list_2.get(6),
                    list_1.get(i), supports.get_current_status(list_2.get(2)), Long.parseLong(list_2.get(3)), true);
            admin_comp_off_holiday_list.add(admin_comp_off_holiday);
        }
        set_recycler_view();
    }

    private void set_recycler_view() {
        admin_comp_off_holiday_adapter = new AdminCompOffHolidayAdapter(context, Filter.filterAdminCompOff(admin_comp_off_holiday_list, type_spinner.getSelectedItemPosition()), isChecked, selected_month, selected_year);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(admin_comp_off_holiday_adapter);
    }


    private void set_initial_data() {
        Calendar cal = Calendar.getInstance();
        int mon = cal.get(Calendar.MONTH) + 1;
        month_spinner.setSelection(mon);
        year_spinner.setSelection(1);
        if (mon < 10) {
            selected_month = Constants.KEY_0 + mon;
        } else {
            selected_month = String.valueOf(mon);
        }
        selected_year = String.valueOf(cal.get(Calendar.YEAR));
        get_request_data();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isChecked = true;
        get_request_data();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isChecked = true;
    }
}