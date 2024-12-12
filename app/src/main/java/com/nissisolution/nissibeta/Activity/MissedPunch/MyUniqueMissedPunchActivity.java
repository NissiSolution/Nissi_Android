package com.nissisolution.nissibeta.Activity.MissedPunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Adapter.Approval.ApprovalAdapter;
import com.nissisolution.nissibeta.Classes.Approval;
import com.nissisolution.nissibeta.Classes.Department;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUniqueMissedPunchActivity extends AppCompatActivity {

    private Context context;
    private LinearLayout rejection_layout;
    private int status = 0;
    private TextView licence, request_date, working_hours, requested_on, reason, worked_period, rejection_reason;
    private RecyclerView recyclerView;
    private PreferencesManager preferencesManager;
    private Supports supports;
    private Intent get_intent;
    private List<Approval> approval_list;
    private List<Department> department_list;
    private ApprovalAdapter approval_adapter;
    private ProgressBar progressBar;
    private String the_data;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_unique_missed_punch);

        declare_items();
        set_listeners();
        set_toolbar();
        get_recycler();
    }

    private void declare_items() {
        context = this;
        licence = findViewById(R.id.licence);
        request_date = findViewById(R.id.requested_date);
        worked_period = findViewById(R.id.worked_period);
        working_hours = findViewById(R.id.working_hours);
        requested_on = findViewById(R.id.requested_on);
        reason = findViewById(R.id.reason);
        recyclerView = findViewById(R.id.recyclerView);
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(context);
        get_intent = getIntent();
        the_data = get_intent.getStringExtra(Constants.KEY_DATA);
        approval_list = new ArrayList<>();
        department_list = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        rejection_reason = findViewById(R.id.rejection_reason);
        rejection_layout = findViewById(R.id.rejection_layout);
    }

    private void set_listeners() {
        supports.licence(licence);
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

    @SuppressLint("SetTextI18n")
    private void get_recycler() {
        List<String> list_1 = Arrays.asList(the_data.split(Constants.KEY_SPLITTER_4));
        request_date.setText(list_1.get(0));
        worked_period.setText("From " + list_1.get(4) + " to " + list_1.get(5));
        working_hours.setText(list_1.get(1) + " hours");
        requested_on.setText(get_date(Long.parseLong(list_1.get(3))));
        reason.setText(list_1.get(7));
        get_staff_info(list_1.get(2));
        rejection_reason.setText(list_1.get(8));
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        return format.format(new Date(timestamp));
    }

    private void get_staff_info(String the_approver) {
        progressBar.setVisibility(View.VISIBLE);
        List<String> list_1 = Arrays.asList(the_approver.split(Constants.KEY_SPLITTER_1));
        List<String> list_2 = Arrays.asList(list_1.get(1).split(Constants.KEY_SPLITTER_2));
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> handle_response(the_approver, response),
                error -> {
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    progressBar.setVisibility(View.GONE);
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_ALL_DEPARTMENTS_INFO);
                params.put(Constants.KEY_STAFF_ID, list_2.get(0));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_response(String the_approver, String the_response) {
        progressBar.setVisibility(View.GONE);
        department_list.clear();
        approval_list.clear();
        List<String> list_1 = Arrays.asList(the_approver.split(Constants.KEY_SPLITTER_1));
        List<String> list_2 = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_1));
        for (int i = 1; i < list_2.size(); i++) {
            List<String> list_3 = Arrays.asList(list_2.get(i).split(Constants.KEY_SPLITTER_2));
            Department department = new Department(Integer.parseInt(list_3.get(0)), Integer.parseInt(list_3.get(1)), list_3.get(2), list_3.get(3));
            department_list.add(department);
        }
        for (int i = 1; i < list_1.size(); i++) {
            List<String> list_4 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
            Approval approval = new Approval(Integer.parseInt(list_4.get(0)), Integer.parseInt(list_4.get(1)), Long.parseLong(list_4.get(2)));
            status = approval.status;
            approval_list.add(approval);
        }

        set_recycler_view();
        set_reason();
    }

    private void set_reason() {
        if (status == 2) {
            rejection_layout.setVisibility(View.VISIBLE);
        } else {
            rejection_layout.setVisibility(View.GONE);
        }
    }

    private void set_recycler_view() {
        approval_adapter = new ApprovalAdapter(context, approval_list, department_list);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(approval_adapter);
    }

}