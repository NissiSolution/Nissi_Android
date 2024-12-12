package com.nissisolution.nissibeta.Activity.Admin;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Adapter.Permission.PermissionAdapter;
import com.nissisolution.nissibeta.Classes.Departments;
import com.nissisolution.nissibeta.Classes.PermissionData;
import com.nissisolution.nissibeta.Classes.PermissionGroup;
import com.nissisolution.nissibeta.Classes.PermissionName;
import com.nissisolution.nissibeta.Classes.PermissionStaff;
import com.nissisolution.nissibeta.Classes.StaffDepartments;
import com.nissisolution.nissibeta.Classes.StaffInfo;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PermissionManagementActivity extends AppCompatActivity {

    private Spinner staffSpinner, departmentSpinner;
    private ArrayAdapter<String> staffAdapter, departmentAdapter;
    private Context context;
    private PreferencesManager preferencesManager;
    private Supports supports;
    private List<String> staffList, displayStaffList, departmentList, displayDepartmentList;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private List<StaffInfo> staffInfoList;
    private List<StaffDepartments> staffDepartmentsList;
    private List<Departments> departmentsList;
    private List<PermissionGroup> permissionGroupList;
    private List<PermissionName> permissionNameList;
    private PermissionAdapter permissionAdapter;
    private List<PermissionData> permissionDataList, staffPermissionDataList;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Button update;
    private int staffId = 0, count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_permission_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        declareItem();
        setListeners();
        getStaff();
        getDepartment();
        setToolbar();
        getPermissionGroup();
    }

    private void declareItem() {
        context = this;
        preferencesManager = new PreferencesManager(context);
        supports = new Supports(context);
        staffSpinner = findViewById(R.id.staff_spinner);
        departmentSpinner = findViewById(R.id.department_spinner);
        staffList = new ArrayList<>();
        displayStaffList = new ArrayList<>();
        departmentList = new ArrayList<>();
        displayDepartmentList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context);
        progressBar = findViewById(R.id.progressBar);
        staffInfoList = new ArrayList<>();
        staffDepartmentsList = new ArrayList<>();
        departmentsList = new ArrayList<>();
        permissionGroupList = new ArrayList<>();
        permissionNameList = new ArrayList<>();
        permissionDataList = new ArrayList<>();
        staffPermissionDataList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        update = findViewById(R.id.update);
    }

    private void setListeners() {
        staffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
                if (position != 0) {
                    getStaffId(staffSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    setStaffSpinner(staffInfoList);
                } else {
                    compareDepartment();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update.setOnClickListener(v -> setPermission());
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.permission_management));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void compareDepartment() {
        int id = 0;
        String  departmentName = departmentSpinner.getSelectedItem().toString();
        for (Departments departments: departmentsList) {
            if (departments.name.equals(departmentName)) {
                id = departments.departmentid;
                break;
            }
        }

        setStaffSpinner(getStaffInfoList(id));

    }

    private List<StaffInfo> getStaffInfoList(int id) {
        List<StaffInfo> staffInfoList = new ArrayList<>();
        List<Integer> staffIdList = new ArrayList<>();
        for (StaffDepartments staffDepartments: staffDepartmentsList) {
            if (Objects.equals(id, staffDepartments.departmentid)) {
                staffIdList.add(staffDepartments.staffid);
            }
        }

        for (StaffInfo staffInfo :
                this.staffInfoList) {
            if (staffIdList.contains(staffInfo.staffid)) {
                staffInfoList.add(staffInfo);
            }
        }
        return staffInfoList;
    }

    private void getStaff() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handleStaff, error -> {progressBar.setVisibility(View.GONE);}) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_ALL_STAFFS);
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {

        }
    }

    private void handleStaff(String response) {
        try {
            progressBar.setVisibility(View.GONE);
            JSONArray jsonArray = new JSONArray(response);
            staffInfoList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                StaffInfo staffInfo = new StaffInfo(jsonObject.getInt(Constants.KEY_STAFFID),
                        jsonObject.getString(Constants.KEY_FIRSTNAME), jsonObject.getString(Constants.KEY_LASTNAME),
                        jsonObject.getString(Constants.KEY_STAFF_IDENTIFI));
                staffInfoList.add(staffInfo);
            }
            setStaffSpinner(staffInfoList);
        } catch (Exception ignored) {

        }
    }

    private void setStaffSpinner(List<StaffInfo> staffInfoList) {
        List<String> staffNameList = new ArrayList<>();
        staffNameList.add("Select");
        for (StaffInfo staffinfo :
                staffInfoList) {
            staffNameList.add(staffinfo.firstname + " " + staffinfo.lastname + " " + staffinfo.staff_identifi);
        }
        staffAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout_3, staffNameList);
        staffSpinner.setAdapter(staffAdapter);
    }

    private void getDepartment() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handleDepartment, error -> progressBar.setVisibility(View.GONE)) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_ALL_DEPARTMENTS);
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {
            supports.create_short_toast("Error");
        }
    }

    private void handleDepartment(String response) {
        try {
            List<String> list1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_2));
            progressBar.setVisibility(View.GONE);
            JSONArray jsonArray1 = new JSONArray(list1.get(0));
            departmentsList.clear();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray1.get(i);
                departmentsList.add(new Departments(jsonObject.getInt(Constants.KEY_DEPARTMENTID), jsonObject.getString(Constants.KEY_NAME)));
            }
            JSONArray jsonArray2 = new JSONArray(list1.get(1));
            staffDepartmentsList.clear();
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray2.get(i);
                staffDepartmentsList.add(new StaffDepartments(jsonObject.getInt(Constants.KEY_STAFFID), jsonObject.getInt(Constants.KEY_DEPARTMENTID)));
            }
            setDepartmentSpinner(departmentsList);
        } catch (Exception ignored) {

        }
    }

    private void setDepartmentSpinner(List<Departments> departmentsList) {
        List<String> departmentNameList = new ArrayList<>();
        departmentNameList.add("All Departments");
        for (Departments departments: departmentsList) {
            departmentNameList.add(departments.name);
        }
        departmentAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout_3, departmentNameList);
        departmentSpinner.setAdapter(departmentAdapter);
    }

    private void getPermissionGroup() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handlePermissionGroup, error -> {progressBar.setVisibility(View.GONE);}) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_PERMISSION_GROUP);
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {

        }
    }

    private void handlePermissionGroup(String response) {
        try {
            progressBar.setVisibility(View.GONE);
            List<String> list1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_2));
            JSONArray jsonArray1 = new JSONArray(list1.get(0));
            JSONArray jsonArray2 = new JSONArray(list1.get(1));

            permissionGroupList.clear();

            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                permissionGroupList.add(new PermissionGroup(jsonObject.getInt(Constants.KEY_PERMISSION_ID), jsonObject.getString(Constants.KEY_NAME)));
            }

            permissionNameList.clear();

            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                permissionNameList.add(new PermissionName(jsonObject.getInt(Constants.KEY_ID), jsonObject.getInt(Constants.KEY_PERMISSION_ID), jsonObject.getString(Constants.KEY_NAME)));
            }
            setPermissionDataList();
        } catch (Exception ignored) {
            supports.create_short_toast(ignored.getMessage());
        }


    }

    private void setPermissionDataList() {
        permissionDataList.clear();
        for (PermissionGroup permissionGroup: permissionGroupList) {
            permissionDataList.add(new PermissionData(0, permissionGroup.name));
            for (PermissionName permissionName :
                    permissionNameList) {
                if (permissionGroup.permission_id == permissionName.permission_id) {
                    permissionDataList.add(new PermissionData(permissionName.id, permissionName.name));
                }
            }
        }
    }

    private void setPermissionRecycler(List<PermissionData> permissionDataList) {
        permissionAdapter = new PermissionAdapter(context, permissionDataList);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(permissionAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void getStaffId(String staffName) {
        for (StaffInfo staffInfo : staffInfoList) {
            String staffN = staffInfo.firstname + " " + staffInfo.lastname + " " + staffInfo.staff_identifi;
            if (Objects.equals(staffN, staffName)) {
                staffId = staffInfo.staffid;
                getPermission(staffId);
                break;
            }
        }
    }
    
    private void getPermission(int staffId) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handlePermission, error -> progressBar.setVisibility(View.GONE)) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_PERMISSION);
                    map.put(Constants.KEY_STAFF_ID, String.valueOf(staffId));
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {

        }
    }

    private void handlePermission(String response) {
        try {
            staffPermissionDataList.clear();
            List<PermissionStaff> permissionStaffList = new ArrayList<>();
            for (PermissionData permissionData :
                    permissionDataList) {
                permissionData.isChecked = false;
            }
            if (!response.isEmpty()) {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    permissionStaffList.add(new PermissionStaff(jsonObject.getInt(Constants.KEY_ID), jsonObject.getInt(Constants.KEY_PERMISSION_ID), jsonObject.getInt(Constants.KEY_STAFFID)));
                }
                for (PermissionData permissionData :
                        permissionDataList) {
                    if (permissionData.id != 0) {
                        for (PermissionStaff permissionStaff : permissionStaffList) {
                            if (permissionStaff.permission_id == permissionData.id) {
                                permissionData.isChecked = true;
                                break;
                            } else {
                                permissionData.isChecked = false;
                            }
                        }
                    }
                    staffPermissionDataList.add(permissionData);
                }
            } else {
                staffPermissionDataList.addAll(permissionDataList);
            }
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
            setPermissionRecycler(staffPermissionDataList);
        } catch (Exception ignored) {
            progressBar.setVisibility(View.GONE);
            supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
        }

    }

    private void setPermission() {
        StringBuilder builder = new StringBuilder();
        count = 0;
        for (PermissionData permissionData :
                staffPermissionDataList) {
            if (permissionData.isChecked) {
                if (builder.toString().isEmpty()) {
                    builder.append(permissionData.id);
                } else {
                    builder.append(",").append(permissionData.id);
                }
                count++;
            }
        }

        try {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    response -> {
                        if (response.equals(Constants.KEY_SUCCESS)) {
                            supports.create_short_toast(Constants.KEY_DATA_SEND_SUCCESSFULLY);
                        } else if (response.equals(Constants.KEY_FAILURE)) {
                            supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
                        }
                    }, error -> {
                supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_SET_PERMISSION);
                    map.put(Constants.KEY_STAFF_ID, String.valueOf(staffId));
                    map.put(Constants.KEY_COUNT, String.valueOf(count));
                    map.put(Constants.KEY_PERMISSION, builder.toString());
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {
            supports.create_short_toast(Constants.KEY_SOMETHING_WENT_WRONG);
        }

    }

}