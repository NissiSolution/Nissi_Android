package com.nissisolution.nissibeta.Activity.Leave;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.nissisolution.nissibeta.Adapter.Approve.ApproveLeaveAdapter;
import com.nissisolution.nissibeta.Classes.AdminLeave;
import com.nissisolution.nissibeta.Classes.LeaveList;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Filter;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MassApproveLeaveActivity extends AppCompatActivity {

    private Button approve, reject;
    private RecyclerView recyclerView;
    private TextView licence;
    private ProgressBar progressBar;
    private PreferencesManager preferencesManager;
    private Supports supports;
    private Context context;
    private List<AdminLeave> admin_leave_list;
    private ApproveLeaveAdapter approve_leave_adapter;
    private LinearLayoutManager layoutManager;
    private Intent get_intent;
    private String selected_month, selected_year;
    private Button selection;
    private boolean isSelected = true;
    private Dialog dialog_reject;
    private TextInputEditText d_reason;
    private Button d_reject, d_cancel;
    private List<LeaveList> leave_list_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_approve_leave);

        declare_item();
        set_listeners();
        set_toolbar();
        get_leave_type();
    }

    private void declare_item() {
        approve = findViewById(R.id.approve);
        reject = findViewById(R.id.reject);
        recyclerView = findViewById(R.id.recyclerView);
        licence = findViewById(R.id.licence);
        progressBar = findViewById(R.id.progressBar);
        context = this;
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(context);
        selection = findViewById(R.id.selection);
        get_intent = getIntent();
        selected_month = get_intent.getStringExtra(Constants.KEY_MONTH);
        selected_year = get_intent.getStringExtra(Constants.KEY_YEAR);
        admin_leave_list = new ArrayList<>();
        dialog_reject = new Dialog(context);
        dialog_reject.setContentView(R.layout.dialog_reject_reason);
        dialog_reject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_reject.setCancelable(false);
        d_reason = dialog_reject.findViewById(R.id.reason);
        d_reject = dialog_reject.findViewById(R.id.reject);
        d_cancel = dialog_reject.findViewById(R.id.cancel);
        leave_list_list = new ArrayList<>();
    }

    private void set_listeners() {
        supports.licence(licence);
        approve.setOnClickListener(v -> approve_request());
        reject.setOnClickListener(v -> reject_request());
        selection.setOnClickListener(v -> select_or_deselect());
        d_cancel.setOnClickListener(view -> dialog_reject.dismiss());
        d_reject.setOnClickListener(v -> {
            if (d_reason.getText().toString().isEmpty()) {
                d_reason.setError(Constants.KEY_REQUIRED);
                d_reason.requestFocus();
            } else {
                approve_or_reject(Constants.KEY_2);
            }
        });
    }

    private void approve_request() {
        boolean ch = false;
        for (AdminLeave h:admin_leave_list) {
            if (h.isChecked) {
                ch = true;
                break;
            }
        }
        if (ch) {
            approve_or_reject(Constants.KEY_1);
        } else {
            supports.create_short_toast("No item selected");
        }
    }

    private void reject_request() {
        boolean ch = false;
        for (AdminLeave h:admin_leave_list) {
            if (h.isChecked) {
                ch = true;
                break;
            }
        }
        if (ch) {
            dialog_reject.show();
        } else {
            supports.create_short_toast("No item selected");
        }
    }

    private void select_or_deselect() {
        if (isSelected) {
            selection.setText(getResources().getText(R.string.de_select_all));
            for (AdminLeave h:
                    admin_leave_list) {
                if (h.status == 0) {
                    h.isChecked = true;
                }
            }
            isSelected = false;
        } else {
            selection.setText(getResources().getText(R.string.select_all));
            for (AdminLeave h:
                    admin_leave_list) {
                if (h.status == 0) {
                    h.isChecked = false;
                }
            }
            isSelected = true;
        }
        set_recycler_view();
    }

    private void set_toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.approve_or_reject));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void get_request_data() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handle_request_data, error -> {
            progressBar.setVisibility(View.GONE);
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_LEAVE_APPROVAL_REQUEST);
                //todo change staff id
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                // params.put(Constants.KEY_STAFF_ID, "10");
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
        admin_leave_list.clear();
        List<String> list_1 = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_3));
        String first_val, second_val;
        first_val = "one";
        second_val = "two";

        for (int i = 1; i < list_1.size(); i++) {
            first_val = list_1.get(i);

            if (!first_val.equals(second_val)) {
                List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_4));
                AdminLeave adminLeave = new AdminLeave(list_2.get(0), get_leave_type(list_2.get(1)),
                        list_2.get(2), list_2.get(3), Integer.parseInt(list_2.get(8)), supports.get_current_status(list_2.get(5)),
                        Integer.parseInt(list_2.get(4)), list_1.get(i), Long.parseLong(list_2.get(7)), false);
                admin_leave_list.add(adminLeave);
                second_val = list_1.get(i);
            }

        }

        set_recycler_view();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_LEAVE_TYPE);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_leave_response(String the_response) {
        List<String> first_list = Arrays.asList(the_response.split(Constants.KEY_SPLITTER_1));
        leave_list_list.clear();
        for (int i = 1; i < first_list.size(); i++) {
            List<String> second_list = Arrays.asList(first_list.get(i).split(Constants.KEY_SPLITTER_2));
            LeaveList current_list = new LeaveList(second_list.get(0), second_list.get(1), second_list.get(2));
            leave_list_list.add(current_list);
        }

        get_request_data();
    }

    private String get_leave_type(String the_type) {
        for (int i = 0; i < leave_list_list.size(); i++) {
            if (leave_list_list.get(i).slug.equals(the_type)) {
                return leave_list_list.get(i).type_name;
            }
        }
        return "Unknown";
    }

    private void set_recycler_view() {
        approve_leave_adapter = new ApproveLeaveAdapter(context, Filter.filterAdminLeave(admin_leave_list, 1), selection, isSelected);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(approve_leave_adapter);
    }

    private void approve_or_reject(String type) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    if (response.equals(Constants.KEY_SUCCESS)) {
                        finish();
                        supports.create_short_toast(Constants.KEY_UPDATE_SUCCESSFUL);
                    } else {
                        supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                    }
                    progressBar.setVisibility(View.GONE);
                }, error -> {
            supports.create_short_toast(error.getMessage());
            progressBar.setVisibility(View.GONE);
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_MASS_APPROVE_OR_REJECT_LEAVE);
                params.put(Constants.KEY_TYPE, type);
                params.put(Constants.KEY_STAFF_ID, get_staff_id());
                params.put(Constants.KEY_ID, get_id());
                params.put(Constants.KEY_DATA, get_key());
                params.put(Constants.KEY_TIMESTAMP, get_timestamp());
                // todo
                params.put(Constants.KEY_APPROVER_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                params.put(Constants.KEY_REASON, d_reason.getText().toString());
                params.put(Constants.KEY_CURRENT_TIMESTAMP, String.valueOf(new Date().getTime()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private String get_staff_id() {
        StringBuilder the_data = new StringBuilder("The data");
        for (AdminLeave h :
                admin_leave_list) {
            List<String> list_1 = Arrays.asList(h.the_data.split(Constants.KEY_SPLITTER_4));
            if (h.isChecked) {
                the_data.append("-").append(list_1.get(10));
            }
        }
        return the_data.toString();
    }

    private String get_id() {
        StringBuilder the_data = new StringBuilder("The data");
        for (AdminLeave h :
                admin_leave_list) {
            List<String> list_1 = Arrays.asList(h.the_data.split(Constants.KEY_SPLITTER_4));
            if (h.isChecked) {
                the_data.append("-").append(list_1.get(6));
            }
        }
        return the_data.toString();
    }

    private String get_key() {
        StringBuilder the_data = new StringBuilder("The data");
        for (AdminLeave h :
                admin_leave_list) {
            List<String> list_1 = Arrays.asList(h.the_data.split(Constants.KEY_SPLITTER_4));
            if (h.isChecked) {
                the_data.append("-").append(list_1.get(4));
            }
        }
        return the_data.toString();
    }

    private String get_timestamp() {
        StringBuilder the_data = new StringBuilder("The data");
        for (AdminLeave h :
                admin_leave_list) {
            List<String> list_1 = Arrays.asList(h.the_data.split(Constants.KEY_SPLITTER_4));
            if (h.isChecked) {
                the_data.append("-").append(list_1.get(7));
            }
        }
        return the_data.toString();
    }
}