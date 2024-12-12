package com.nissisolution.nissibeta.Activity.HumanResources;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Activity.Admin.PermissionManagementActivity;
import com.nissisolution.nissibeta.Activity.CompOffHoliday.AdminCompOffActivity;
import com.nissisolution.nissibeta.Activity.CompOffHoliday.CompOffHolidayActivity;
import com.nissisolution.nissibeta.Activity.Leave.AdminLeaveActivity;
import com.nissisolution.nissibeta.Activity.Leave.LeaveActivity;
import com.nissisolution.nissibeta.Activity.Locations.AdminLocationActivity;
import com.nissisolution.nissibeta.Activity.Locations.AdminMapsActivity;
import com.nissisolution.nissibeta.Activity.MissedPunch.AdminMissedPunchActivity;
import com.nissisolution.nissibeta.Activity.MissedPunch.MissedPunchActivity;
import com.nissisolution.nissibeta.Activity.NightShift.AdminNightShiftActivity;
import com.nissisolution.nissibeta.Activity.NightShift.NightShiftActivity;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HrActivity extends AppCompatActivity {

    private TextView licence;
    private PreferencesManager preferencesManager;
    private ProgressBar progressBar;
    private Context context;
    private Supports supports;
    private LinearLayout a_layout_1, a_layout_2, a_layout_3, a_layout_4, a_layout_5, a_layout_6, a_layout_7, a_layout_8, a_layout_9, a_layout_10,
            e_layout_1, e_layout_2, e_layout_3, e_layout_4, admin_layout_1, admin_layout_2;
    private ImageView a_leave_request_i, a_approve_leave_i, a_comp_off_request_i, a_approve_comp_off_i,
            a_missed_punch_i, a_approve_missed_punch_i, a_night_shift_i, a_approve_night_shift_i,
            e_leave_request_i, e_missed_punch_i, e_comp_off_i, e_night_shift_i,
            a_map_location_i, a_checked_info_i, permission_management_i;
    private TextView a_leave_request_t, a_approve_leave_t, a_comp_off_request_t, a_approve_comp_off_t,
            a_missed_punch_t, a_approve_missed_punch_t, a_night_shift_t, a_approve_night_shift_t,
            e_leave_request_t, e_missed_punch_t, e_comp_off_t, e_night_shift_t,
            a_map_location_t, a_checked_info_t, permission_management_t;
    private TextView aLeaveCount, aLeaveRequestCount, aMissedPunchCount, aMissedPunchRequestCount,
            aCompOffHolidayCount, aCompOffHolidayRequestCount, aNightShiftCount, aNightShiftRequestCount,
            eLeaveCount, eMissedPunchCount, eCompOffHolidayCount, eNightShiftCount;
    private RelativeLayout aLeaveCountLayout, aLeaveRequestCountLayout, aMissedPunchCountLayout, aMissedPunchRequestCountLayout,
            aCompOffHolidayCountLayout, aCompOffHolidayRequestCountLayout, aNightShiftCountLayout, aNightShiftRequestCountLayout,
            eLeaveCountLayout, eMissedPunchCountLayout, eCompOffHolidayCountLayout, eNightShiftCountLayout;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        declare_item();
        set_listeners();
        set_toolbar();
        get_admin();
        checkTrueAdmin();
//        preferencesManager.putString(Constants.KEY_STAFF_ID, "270");
    }

    private void declare_item() {
        context = this;
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(context);
        progressBar = findViewById(R.id.progressBar);
        licence = findViewById(R.id.licence);
        a_layout_1 = findViewById(R.id.a_layout_1);
        a_layout_1.setVisibility(View.GONE);
        a_layout_2 = findViewById(R.id.a_layout_2);
        a_layout_2.setVisibility(View.GONE);
        a_layout_3 = findViewById(R.id.a_layout_3);
        a_layout_3.setVisibility(View.GONE);
        a_layout_4 = findViewById(R.id.a_layout_4);
        a_layout_4.setVisibility(View.GONE);
        a_layout_5 = findViewById(R.id.a_layout_5);
        a_layout_5.setVisibility(View.GONE);
        a_layout_6 = findViewById(R.id.a_layout_6);
        a_layout_6.setVisibility(View.GONE);
        a_layout_7 = findViewById(R.id.a_layout_7);
        a_layout_7.setVisibility(View.GONE);
        a_layout_8 = findViewById(R.id.a_layout_8);
        a_layout_8.setVisibility(View.GONE);
        a_layout_9 = findViewById(R.id.a_layout_9);
        a_layout_9.setVisibility(View.GONE);
        a_layout_10 = findViewById(R.id.a_layout_10);
        a_layout_10.setVisibility(View.GONE);
        e_layout_1 = findViewById(R.id.e_layout_1);
        e_layout_2 = findViewById(R.id.e_layout_2);
        e_layout_3 = findViewById(R.id.e_layout_3);
        e_layout_4 = findViewById(R.id.e_layout_4);
        a_leave_request_i = findViewById(R.id.a_leave_image);
        a_approve_leave_i = findViewById(R.id.a_approve_leave_image);
        a_comp_off_request_i = findViewById(R.id.a_comp_off_image);
        a_approve_comp_off_i = findViewById(R.id.a_approve_comp_off_image);
        a_missed_punch_i = findViewById(R.id.a_missed_punch_image);
        a_approve_missed_punch_i = findViewById(R.id.a_approve_missed_punch_image);
        a_night_shift_i = findViewById(R.id.a_night_shift_image);
        a_approve_night_shift_i = findViewById(R.id.a_approve_night_shift_image);
        e_leave_request_i = findViewById(R.id.e_leave_image);
        e_missed_punch_i = findViewById(R.id.e_missed_punch_image);
        e_comp_off_i = findViewById(R.id.e_comp_off_image);
        e_night_shift_i = findViewById(R.id.e_night_shift_image);
        a_leave_request_t = findViewById(R.id.a_leave_text);
        a_approve_leave_t = findViewById(R.id.a_approve_leave_text);
        a_comp_off_request_t = findViewById(R.id.a_comp_off_text);
        a_approve_comp_off_t = findViewById(R.id.a_approve_comp_off_text);
        a_missed_punch_t = findViewById(R.id.a_missed_punch_text);
        a_approve_missed_punch_t = findViewById(R.id.a_approve_missed_punch_text);
        a_night_shift_t = findViewById(R.id.a_night_shift_text);
        a_approve_night_shift_t = findViewById(R.id.a_approve_night_shift_text);
        e_leave_request_t = findViewById(R.id.e_leave_text);
        e_missed_punch_t = findViewById(R.id.e_missed_punch_text);
        e_comp_off_t = findViewById(R.id.e_comp_off_text);
        e_night_shift_t = findViewById(R.id.e_night_shift_text);
        a_map_location_i = findViewById(R.id.a_map_location_image);
        a_checked_info_i = findViewById(R.id.a_checked_info_image);
        a_map_location_t = findViewById(R.id.a_map_location_text);
        a_checked_info_t = findViewById(R.id.a_checked_info_text);

        admin_layout_1 = findViewById(R.id.admin_layout_1);
        admin_layout_2 = findViewById(R.id.admin_layout_2);
        permission_management_i = findViewById(R.id.permission_management_i);
        permission_management_t = findViewById(R.id.permission_management_t);

        aLeaveCount = findViewById(R.id.aLeaveCount);
        aLeaveRequestCount= findViewById(R.id.aLeaveRequestCount);
        aMissedPunchCount = findViewById(R.id.aMissedPunchCount);
        aMissedPunchRequestCount = findViewById(R.id.aMissedPunchRequestCount);
        aCompOffHolidayCount = findViewById(R.id.aCompOffHolidayCount);
        aCompOffHolidayRequestCount = findViewById(R.id.aCompOffHolidayRequestCount);
        aNightShiftCount = findViewById(R.id.aNightShiftCount);
        aNightShiftRequestCount = findViewById(R.id.aNightShiftRequestCount);
        eLeaveCount = findViewById(R.id.eLeaveCount);
        eMissedPunchCount = findViewById(R.id.eMissedPunchCount);
        eCompOffHolidayCount = findViewById(R.id.eCompOffHolidayCount);
        eNightShiftCount = findViewById(R.id.eNightShiftCount);

        aLeaveCountLayout = findViewById(R.id.aLeaveCountLayout);
        aLeaveRequestCountLayout= findViewById(R.id.aLeaveRequestCountLayout);
        aMissedPunchCountLayout = findViewById(R.id.aMissedPunchCountLayout);
        aMissedPunchRequestCountLayout = findViewById(R.id.aMissedPunchRequestCountLayout);
        aCompOffHolidayCountLayout = findViewById(R.id.aCompOffHolidayCountLayout);
        aCompOffHolidayRequestCountLayout = findViewById(R.id.aCompOffHolidayRequestCountLayout);
        aNightShiftCountLayout = findViewById(R.id.aNightShiftCountLayout);
        aNightShiftRequestCountLayout = findViewById(R.id.aNightShiftRequestCountLayout);
        eLeaveCountLayout = findViewById(R.id.eLeaveCountLayout);
        eMissedPunchCountLayout = findViewById(R.id.eMissedPunchCountLayout);
        eCompOffHolidayCountLayout = findViewById(R.id.eCompOffHolidayCountLayout);
        eNightShiftCountLayout = findViewById(R.id.eNightShiftCountLayout);

        requestQueue = Volley.newRequestQueue(context);
    }

    private void set_listeners() {
        supports.licence(licence);

        supports.start_activity_listener(a_leave_request_i, LeaveActivity.class);
        supports.start_activity_listener(a_missed_punch_i, MissedPunchActivity.class);
        supports.start_activity_listener(a_comp_off_request_i, CompOffHolidayActivity.class);
        supports.start_activity_listener(a_night_shift_i, NightShiftActivity.class);
        supports.start_activity_listener(e_leave_request_i, LeaveActivity.class);
        supports.start_activity_listener(e_missed_punch_i, MissedPunchActivity.class);
        supports.start_activity_listener(e_comp_off_i, CompOffHolidayActivity.class);
        supports.start_activity_listener(e_night_shift_i, NightShiftActivity.class);
        supports.start_activity_listener(a_approve_leave_i, AdminLeaveActivity.class);
        supports.start_activity_listener(a_approve_missed_punch_i, AdminMissedPunchActivity.class);
        supports.start_activity_listener(a_approve_comp_off_i, AdminCompOffActivity.class);
        supports.start_activity_listener(a_approve_night_shift_i, AdminNightShiftActivity.class);
        supports.start_activity_listener(a_map_location_i, AdminMapsActivity.class);
        supports.start_activity_listener(a_checked_info_i, AdminLocationActivity.class);

        supports.start_activity_listener(a_leave_request_t, LeaveActivity.class);
        supports.start_activity_listener(a_missed_punch_t, MissedPunchActivity.class);
        supports.start_activity_listener(a_comp_off_request_t, CompOffHolidayActivity.class);
        supports.start_activity_listener(a_night_shift_t, NightShiftActivity.class);
        supports.start_activity_listener(e_leave_request_t, LeaveActivity.class);
        supports.start_activity_listener(e_missed_punch_t, MissedPunchActivity.class);
        supports.start_activity_listener(e_comp_off_t, CompOffHolidayActivity.class);
        supports.start_activity_listener(e_night_shift_t, NightShiftActivity.class);
        supports.start_activity_listener(a_approve_leave_t, AdminLeaveActivity.class);
        supports.start_activity_listener(a_approve_missed_punch_t, AdminMissedPunchActivity.class);
        supports.start_activity_listener(a_approve_comp_off_t, AdminCompOffActivity.class);
        supports.start_activity_listener(a_approve_night_shift_t, AdminNightShiftActivity.class);
        supports.start_activity_listener(a_map_location_t, AdminMapsActivity.class);
        supports.start_activity_listener(a_checked_info_t, AdminLocationActivity.class);
        supports.start_activity_listener(permission_management_t, PermissionManagementActivity.class);
        supports.start_activity_listener(permission_management_i, PermissionManagementActivity.class);
    }

    private void set_toolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.human_resources));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void get_admin() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::set_admin,
                error -> {
                    progressBar.setVisibility(View.GONE);
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_IS_ADMIN);
                params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void set_admin(String the_response) {
        progressBar.setVisibility(View.GONE);
        if (the_response.equals(Constants.KEY_ADMIN)) {
            a_layout_1.setVisibility(View.VISIBLE);
            a_layout_2.setVisibility(View.VISIBLE);
            a_layout_3.setVisibility(View.VISIBLE);
            a_layout_4.setVisibility(View.VISIBLE);
            a_layout_5.setVisibility(View.VISIBLE);
            a_layout_6.setVisibility(View.VISIBLE);
            a_layout_7.setVisibility(View.VISIBLE);
            a_layout_8.setVisibility(View.VISIBLE);
            a_layout_9.setVisibility(View.VISIBLE);
            a_layout_10.setVisibility(View.VISIBLE);
            e_layout_1.setVisibility(View.GONE);
            e_layout_2.setVisibility(View.GONE);
            e_layout_3.setVisibility(View.GONE);
            e_layout_4.setVisibility(View.GONE);
        } else {
            a_layout_1.setVisibility(View.GONE);
            a_layout_2.setVisibility(View.GONE);
            a_layout_3.setVisibility(View.GONE);
            a_layout_4.setVisibility(View.GONE);
            a_layout_5.setVisibility(View.GONE);
            a_layout_6.setVisibility(View.GONE);
            a_layout_7.setVisibility(View.GONE);
            a_layout_8.setVisibility(View.GONE);
            a_layout_9.setVisibility(View.GONE);
            a_layout_10.setVisibility(View.GONE);
            e_layout_1.setVisibility(View.VISIBLE);
            e_layout_2.setVisibility(View.VISIBLE);
            e_layout_3.setVisibility(View.VISIBLE);
            e_layout_4.setVisibility(View.VISIBLE);
        }
    }

    private void checkNotification() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handleNotification,
                    error -> new Handler().postDelayed(this::checkNotification, 60 * 1000)) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_NOTIFICATION);
                    map.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {

        }
    }

    private void handleNotification(String response) {

        List<String> list1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_1));

        int leaveCount = 0, leaveRequestCount = 0, missedPunchCount = 0, missedPunchRequestCount = 0,
                compOffHolidayCount = 0, compOffHolidayRequestCount = 0, nightShiftCount = 0, nightShiftRequestCount = 0;

        for (int i = 1; i < list1.size(); i++) {
            List<String> list2 = Arrays.asList(list1.get(i).split(Constants.KEY_SPLITTER_2));
            if (Integer.parseInt(list2.get(5)) == 0) {
                switch (Integer.parseInt(list2.get(2))) {
                    case 0:
                        leaveCount++;
                        break;
                    case 100:
                        leaveRequestCount++;
                        break;
                    case 1:
                    case 2:
                        compOffHolidayCount++;
                        break;
                    case 101:
                    case 102:
                        compOffHolidayRequestCount++;
                        break;
                    case 3:
                        nightShiftCount++;
                        break;
                    case 103:
                        nightShiftRequestCount++;
                        break;
                    case 10:
                        missedPunchCount++;
                        break;
                    case 110:
                        missedPunchRequestCount++;
                        break;
                    default:
                        break;
                }
            }
        }

        if (leaveCount > 0) {
            aLeaveCountLayout.setVisibility(View.VISIBLE);
            aLeaveCount.setText(String.valueOf(leaveCount));
            eLeaveCountLayout.setVisibility(View.VISIBLE);
            eLeaveCount.setText(String.valueOf(leaveCount));
        } else {
            aLeaveCountLayout.setVisibility(View.GONE);
            eLeaveCountLayout.setVisibility(View.GONE);
        }

        if (leaveRequestCount > 0) {
            aLeaveRequestCountLayout.setVisibility(View.VISIBLE);
            aLeaveRequestCount.setText(String.valueOf(leaveRequestCount));
        } else {
            aLeaveRequestCountLayout.setVisibility(View.GONE);
        }

        if (missedPunchCount > 0) {
            aMissedPunchCountLayout.setVisibility(View.VISIBLE);
            aMissedPunchCount.setText(String.valueOf(missedPunchCount));
            eMissedPunchCountLayout.setVisibility(View.VISIBLE);
            eMissedPunchCount.setText(String.valueOf(missedPunchCount));
        } else {
            aMissedPunchCountLayout.setVisibility(View.GONE);
            eMissedPunchCountLayout.setVisibility(View.GONE);
        }

        if (missedPunchRequestCount > 0) {
            aMissedPunchRequestCountLayout.setVisibility(View.VISIBLE);
            aMissedPunchRequestCount.setText(String.valueOf(missedPunchRequestCount));
        } else {
            aMissedPunchRequestCountLayout.setVisibility(View.GONE);
        }

        if (compOffHolidayCount > 0) {
            aCompOffHolidayCountLayout.setVisibility(View.VISIBLE);
            aCompOffHolidayCount.setText(String.valueOf(compOffHolidayCount));
            eCompOffHolidayCountLayout.setVisibility(View.VISIBLE);
            eCompOffHolidayCount.setText(String.valueOf(compOffHolidayCount));
        } else {
            aCompOffHolidayCountLayout.setVisibility(View.GONE);
            eCompOffHolidayCountLayout.setVisibility(View.GONE);
        }

        if (compOffHolidayRequestCount > 0) {
            aCompOffHolidayRequestCountLayout.setVisibility(View.VISIBLE);
            aCompOffHolidayRequestCount.setText(String.valueOf(compOffHolidayRequestCount));
        } else {
            aCompOffHolidayRequestCountLayout.setVisibility(View.GONE);
        }

        if (nightShiftCount > 0) {
            aNightShiftCountLayout.setVisibility(View.VISIBLE);
            aNightShiftCount.setText(String.valueOf(nightShiftCount));
            eNightShiftCountLayout.setVisibility(View.VISIBLE);
            eNightShiftCount.setText(String.valueOf(nightShiftCount));
        } else {
            aNightShiftCountLayout.setVisibility(View.GONE);
            eNightShiftCountLayout.setVisibility(View.GONE);
        }

        if (nightShiftRequestCount > 0) {
            aNightShiftRequestCountLayout.setVisibility(View.VISIBLE);
            aNightShiftRequestCount.setText(String.valueOf(nightShiftRequestCount));
        } else {
            aNightShiftRequestCountLayout.setVisibility(View.GONE);
        }

        new Handler().postDelayed(this::checkNotification, 60 * 1000);
    }

    private void checkTrueAdmin() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handleTrueAdmin, error -> {supports.create_short_toast(error.getMessage());}) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_IS_TRUE_ADMIN);
                    params.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                    return params;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {
            supports.create_short_toast(ignored.getMessage());
        }
    }

    private void handleTrueAdmin(String response) {
        if (response.equals(Constants.KEY_ADMIN)) {
            admin_layout_1.setVisibility(View.VISIBLE);
            admin_layout_2.setVisibility(View.VISIBLE);
        } else {
            admin_layout_1.setVisibility(View.GONE);
            admin_layout_2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotification();
    }
}