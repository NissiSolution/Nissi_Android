package com.nissisolution.nissibeta.Activity.HumanResources;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApprovalRequestActivity extends AppCompatActivity {

    private TextView licence;
    private ArrayAdapter<String> month_adapter, year_adapter;
    private Spinner month_spinner, year_spinner;
    private RecyclerView recyclerView;
    private Supports supports;
    private PreferencesManager preferencesManager;
    private Context context;
    private String selected_year, selected_month;
    private List<String> month_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_request);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        set_toolbar();
        declare_item();
        set_listeners();
        set_spinners();
    }

    private void declare_item() {
        licence = findViewById(R.id.licence);
        context = this;
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(this);
        recyclerView = findViewById(R.id.recyclerView);
        month_spinner = findViewById(R.id.month);
        year_spinner = findViewById(R.id.year);
        month_array = new ArrayList<>();
    }

    private void set_listeners() {
        supports.licence(licence);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void set_spinners() {
        year_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, getResources().getStringArray(R.array.month_array));
        year_spinner.setAdapter(year_adapter);
        month_array.add(Constants.KEY_SELECT);
        for (int i = get_year(); i > 2020; i--) {
            month_array.add(String.valueOf(i));
        }
        month_adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, month_array);
        month_spinner.setAdapter(month_adapter);
    }

    private void set_toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.approval_request));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_REQUEST_DATA);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_MONTH, selected_month);
                params.put(Constants.KEY_YEAR, selected_year);
                return params;
            }
        };
    }

    private void handle_request_data(String the_response) {

    }

}