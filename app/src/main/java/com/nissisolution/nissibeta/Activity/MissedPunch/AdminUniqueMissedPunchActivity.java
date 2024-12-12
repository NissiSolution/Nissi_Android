package com.nissisolution.nissibeta.Activity.MissedPunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
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

public class AdminUniqueMissedPunchActivity extends AppCompatActivity {

    private LinearLayout rejection_layout;
    private TextView rejection_reason;
    private int status = 0;
    private Context context;
    private TextView staff_name, position, request_date, requested_on, reason, licence, worked_hour;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PreferencesManager preferencesManager;
    private Supports supports;
    private Intent get_intent;
    private String the_data, timestamp, staff_id;
    private LinearLayoutManager layoutManager;
    private ApprovalAdapter approval_adapter;
    private List<Approval> approval_list;
    private List<Department> department_list;
    private Button approve, reject;
    private Dialog reject_dialog;
    private TextInputEditText d_reason;
    private Button d_cancel, d_reject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_unique_missed_punch);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        declare_item();
        set_listeners();
        set_toolbar();
        get_recycler();
    }
    private void declare_item() {
        context = this;
        licence = findViewById(R.id.licence);
        staff_name = findViewById(R.id.staff_name);
        position = findViewById(R.id.position);
        request_date = findViewById(R.id.requested_date);
        requested_on = findViewById(R.id.requested_on);
        reason = findViewById(R.id.reason);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(context);
        get_intent = getIntent();
        the_data = get_intent.getStringExtra(Constants.KEY_DATA);
        approval_list = new ArrayList<>();
        department_list = new ArrayList<>();
        worked_hour = findViewById(R.id.working_hours);
        approve = findViewById(R.id.approve);
        reject = findViewById(R.id.reject);
        rejection_reason = findViewById(R.id.rejection_reason);
        rejection_layout = findViewById(R.id.rejection_layout);
        reject_dialog = new Dialog(context);
        reject_dialog.setContentView(R.layout.dialog_reject_reason);
        reject_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d_reason = reject_dialog.findViewById(R.id.reason);
        d_cancel = reject_dialog.findViewById(R.id.cancel);
        d_reject = reject_dialog.findViewById(R.id.reject);
    }

    private void set_listeners() {
        supports.licence(licence);
        approve.setOnClickListener(view -> approve_or_reject(Constants.KEY_1));
        reject.setOnClickListener(v -> reject_dialog.show());
        supports.edit_text_watcher(d_reason);
        d_reject.setOnClickListener(view -> {
            if (d_reason.getText().toString().trim().isEmpty()) {
                d_reason.setError(Constants.KEY_REQUIRED);
                d_reason.requestFocus();
            } else {
                approve_or_reject(Constants.KEY_2);
            }
        });
        d_cancel.setOnClickListener(v -> reject_dialog.dismiss());
    }

    private void set_toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.missed_punch_information));
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
        staff_name.setText(list_1.get(5));
        timestamp = list_1.get(3);
        requested_on.setText(get_date(Long.parseLong(list_1.get(3))));
        reason.setText(list_1.get(4));
        get_staff_info(list_1.get(2));
        handle_staff_selection(list_1.get(2));
        position.setText(list_1.get(7));
        worked_hour.setText("From " + list_1.get(9) + " to " + list_1.get(10));
        staff_id = list_1.get(8);
        rejection_reason.setText(list_1.get(11));
    }

    private void handle_staff_selection(String the_approver) {
        List<String> list_1 = Arrays.asList(the_approver.split(Constants.KEY_SPLITTER_1));
        for (int i = 1; i < list_1.size(); i ++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
            //todo change staff_id
            if (list_2.get(0).equals(preferencesManager.getString(Constants.KEY_STAFF_ID))) {
                if (!list_2.get(2).equals(Constants.KEY_0)) {
                    approve.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                } else {
                    approve.setVisibility(View.VISIBLE);
                    approve.setVisibility(View.VISIBLE);
                }
            }
        }
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

    private void approve_or_reject(String type) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    if (response.equals(Constants.KEY_FAILURE)) {
                        supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    } else {
                        get_staff_info(response);
                        handle_staff_selection(response);
                        rejection_reason.setText(d_reason.getText().toString());
                        d_reason.setError(null);
                        reject_dialog.hide();
                    }
                }, error -> supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG))
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_APPROVE_OR_REJECT);
                params.put(Constants.KEY_TYPE, type);
                params.put(Constants.KEY_STAFF_ID, staff_id);
                params.put(Constants.KEY_ID, "10");
                params.put(Constants.KEY_TIMESTAMP, timestamp);
                // todo
                params.put(Constants.KEY_APPROVER_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                // params.put(Constants.KEY_APPROVER_ID, "4");
                params.put(Constants.KEY_REASON, d_reason.getText().toString().trim());
                params.put(Constants.KEY_CURRENT_TIMESTAMP, String.valueOf(new Date().getTime()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }
}