package com.nissisolution.nissibeta.Activity.Leave;

import androidx.annotation.NonNull;
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

public class AdminUniqueLeaveActivity extends AppCompatActivity {

    private LinearLayout rejection_layout;
    private TextView rejection_reason;
    private int status = 0;
    private TextView leave_type, purpose, requested_on, duration, reason, licence, staff_name, department, period, as_on_date;
    private RecyclerView recyclerView;
    private Context context;
    private PreferencesManager preferencesManager;
    private Supports supports;
    private Intent get_intent;
    private List<Approval> approval_list;
    private List<Department> department_list;
    private ApprovalAdapter approval_adapter;
    private ProgressBar progressBar;
    private String the_data, request_type, staff_id, timestamp, id, key;
    private LinearLayoutManager layoutManager;
    private Button approve, reject;
    private double available_days, applied_days;
    private Dialog dialog_reject, dialog_approve;
    private TextInputEditText d_reason;
    private Button d_reject, d_cancel_1, d_approve, d_cancel_2;
    private LinearLayout firstHalfLayout, secondHalfLayout;
    private TextView firstHalf, secondHalf;
    private int halfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_unique_leave);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        declare_items();
        set_listeners();
        set_toolbar();
        get_recycler();
    }

    private void declare_items() {
        context = this;
        duration = findViewById(R.id.duration);
        leave_type = findViewById(R.id.leave_type);
        purpose = findViewById(R.id.purpose);
        licence = findViewById(R.id.licence);
        requested_on = findViewById(R.id.requested_on);
        reason = findViewById(R.id.reason);
        recyclerView = findViewById(R.id.recyclerView);
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(context);
        get_intent = getIntent();
        the_data = get_intent.getStringExtra(Constants.KEY_DATA);
        request_type = get_intent.getStringExtra(Constants.KEY_REQUEST_TYPE);
        approval_list = new ArrayList<>();
        department_list = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        staff_name = findViewById(R.id.staff_name);
        department = findViewById(R.id.department);
        period = findViewById(R.id.period);
        as_on_date = findViewById(R.id.as_on_date);
        approve = findViewById(R.id.approve);
        reject = findViewById(R.id.reject);
        dialog_reject = new Dialog(context);
        dialog_reject.setContentView(R.layout.dialog_reject_reason);
        d_reason = dialog_reject.findViewById(R.id.reason);
        d_reject = dialog_reject.findViewById(R.id.reject);
        d_cancel_1 = dialog_reject.findViewById(R.id.cancel);
        dialog_reject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_approve = new Dialog(context);
        dialog_approve.setContentView(R.layout.dialog_approval_error);
        d_cancel_2 = dialog_approve.findViewById(R.id.cancel);
        d_approve = dialog_approve.findViewById(R.id.approve);
        dialog_approve.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rejection_reason = findViewById(R.id.rejection_reason);
        rejection_layout = findViewById(R.id.rejection_layout);
        firstHalf = findViewById(R.id.firstHalf);
        firstHalfLayout = findViewById(R.id.firstHalfLayout);
        secondHalf = findViewById(R.id.secondHalf);
        secondHalfLayout = findViewById(R.id.secondHalfLayout);
    }

    private void set_listeners() {
        supports.licence(licence);
        approve.setOnClickListener(view -> check_approval_condition(Constants.KEY_1));
        reject.setOnClickListener(view -> check_approval_condition(Constants.KEY_2));
        d_cancel_1.setOnClickListener(view -> dialog_reject.dismiss());
        d_cancel_2.setOnClickListener(v -> dialog_approve.dismiss());
        d_approve.setOnClickListener(v -> approve_or_reject(Constants.KEY_1));
        supports.edit_text_watcher(d_reason);
        d_reject.setOnClickListener(v -> {
            if (d_reason.getText().toString().isEmpty()) {
                d_reason.setError(Constants.KEY_REQUIRED);
                d_reason.requestFocus();
            } else {
                approve_or_reject(Constants.KEY_2);
            }
        });

    }

    private void set_toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.leave_information));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @SuppressLint("SetTextI18n")
    private void get_recycler() {
        List<String> list_1 = Arrays.asList(the_data.split(Constants.KEY_SPLITTER_4));
        staff_name.setText(list_1.get(0));
        department.setText(list_1.get(9));
        requested_on.setText(get_date(Long.parseLong(list_1.get(7))));
        leave_type.setText(request_type);
        get_leave_data(list_1.get(4));
        get_staff_info(list_1.get(5));
        handle_staff_selection(list_1.get(5));
        staff_id = list_1.get(10);
        timestamp = list_1.get(7);
        id = list_1.get(6);
        key = list_1.get(4);
        available_days = Double.parseDouble(list_1.get(11));
        if (available_days <= 1) {
            as_on_date.setText(list_1.get(11) + " day");
        } else {
            as_on_date.setText(list_1.get(11) + " days");
        }
        rejection_reason.setText(list_1.get(12));
        halfDay = Integer.parseInt(list_1.get(13));

        switch (halfDay) {
            case 1:
                firstHalfLayout.setVisibility(View.VISIBLE);
                secondHalfLayout.setVisibility(View.GONE);
                break;
            case 2:
                firstHalfLayout.setVisibility(View.GONE);
                secondHalfLayout.setVisibility(View.VISIBLE);
                break;
            case 3:
                firstHalfLayout.setVisibility(View.VISIBLE);
                secondHalfLayout.setVisibility(View.VISIBLE);
                break;
            default:
                firstHalfLayout.setVisibility(View.GONE);
                secondHalfLayout.setVisibility(View.GONE);
                break;
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
                response -> handle_response2(the_approver, response),
                error -> {
                    supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    progressBar.setVisibility(View.GONE);
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_ALL_DEPARTMENTS_INFO);
                params.put(Constants.KEY_STAFF_ID, list_2.get(0));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_response2(String the_approver, String the_response) {
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

    private void get_leave_data(String the_data) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_response,
                error -> supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_CURRENT_LEAVE_DATA);
                params.put(Constants.KEY_TYPE, the_data);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @SuppressLint("SetTextI18n")
    private void handle_response(String the_response) {
        List<String> list_a = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_2));
        purpose.setText(list_a.get(2));
        List<String> list_1 = Arrays.asList(list_a.get(0).split(" "));
        List<String> list_2 = Arrays.asList(list_a.get(1).split(" "));
        applied_days = Double.parseDouble(list_a.get(6));
        if (list_a.get(0).equals(list_a.get(1))) {
            period.setText("On " + get_date_2(list_1.get(0)));
            duration.setText(list_a.get(6) + " day");
        } else {
            period.setText("From " + get_date_2(list_1.get(0)) + " to " + get_date_2(list_2.get(0)));
            if (list_a.get(6).equals(Constants.KEY_1)) {
                duration.setText(list_a.get(6) + " day");
            } else {
                duration.setText(list_a.get(6) + " days");
            }
        }
        reason.setText(list_a.get(3));

        firstHalf.setText(get_date_2(list_1.get(0)));
        secondHalf.setText(get_date_2(list_2.get(0)));
    }

    private String get_date_2(String the_date) {
        List<String> list_1 = Arrays.asList(the_date.split("-"));
        return list_1.get(2) + "-" + list_1.get(1) + "-" + list_1.get(0);
    }

    private void check_approval_condition(String type) {
        if (type.equals(Constants.KEY_1)) {
            if (applied_days <= available_days) {
                approve_or_reject(type);
            } else {
                dialog_approve.show();
            }
        } else {
            dialog_reject.show();
        }
    }

    private void approve_or_reject(String type) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    if (response.equals(Constants.KEY_FAILURE)) {
                        supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    } else {
                        get_staff_info(response);
                        handle_staff_selection(response);
                        rejection_reason.setText(d_reason.getText().toString().trim());
                        dialog_reject.hide();
                        d_reason.setText(null);
                        dialog_approve.hide();
                    }
//                    supports.alert_dialog(response);
                }, error -> supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG))
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_APPROVE_OR_REJECT_LEAVE);
                params.put(Constants.KEY_TYPE, type);
                params.put(Constants.KEY_STAFF_ID, staff_id);
                params.put(Constants.KEY_ID, id);
                params.put(Constants.KEY_DATA, key);
                params.put(Constants.KEY_TIMESTAMP, timestamp);
                // todo
                params.put(Constants.KEY_APPROVER_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_REASON, d_reason.getText().toString());
                //params.put(Constants.KEY_APPROVER_ID, "4");
                params.put(Constants.KEY_CURRENT_TIMESTAMP, String.valueOf(new Date().getTime()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_staff_selection(String the_approver) {
        List<String> list_1 = Arrays.asList(the_approver.split(Constants.KEY_SPLITTER_1));
        for (int i = 1; i < list_1.size(); i ++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
            //todo change staff_id
            if (list_2.get(0).equals(preferencesManager.getString(Constants.KEY_STAFF_ID))) {
                if (!list_2.get(1).equals(Constants.KEY_0)) {
                    approve.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                } else {
                    reject.setVisibility(View.VISIBLE);
                    approve.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}