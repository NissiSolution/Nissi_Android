package com.nissisolution.nissibeta.Activity.GuestHouse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.google.android.material.imageview.ShapeableImageView;
import com.nissisolution.nissibeta.Adapter.GuestHouseApplication.GuestHouseApplicationAdapter;
import com.nissisolution.nissibeta.Classes.GuestHouseApplication;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestHouseApplicationActivity extends AppCompatActivity {

    private int position, id;
    private String name, mapUrl, imageUrl;
    protected ShapeableImageView guestHouseImage, mapNavigate;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Context context;
    private Supports supports;
    private PreferencesManager preferencesManager;
    private TextView guestHouseName, guestHouseCapacity, guestHousePopulation, guestHouseAvailability;
    private LinearLayoutManager layoutManager;
    private Spinner monthSpinner, yearSpinner, typeSpinner;
    private List<String> monthList, yearList, typeList;
    private ArrayAdapter<String> monthAdapter, yearAdapter, typeAdapter;
    private List<GuestHouseApplication> guestHouseApplicationList;
    private GuestHouseApplicationAdapter guestHouseApplicationAdapter;
    private RequestQueue requestQueue;
    private long fromTime, toTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guest_house_application);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        declareItem();
        setToolbar();
        setListeners();
        getGuestHouseInfo();
        setInitial();
    }

    private void declareItem() {
        monthSpinner = findViewById(R.id.month);
        yearSpinner = findViewById(R.id.year);
        typeSpinner = findViewById(R.id.type);
        id = getIntent().getIntExtra(Constants.KEY_ID, -1);
        position = getIntent().getIntExtra(Constants.KEY_POSITION, -1);
        name = getIntent().getStringExtra(Constants.KEY_NAME);
        mapUrl = getIntent().getStringExtra(Constants.KEY_MAP_URL);
        imageUrl = getIntent().getStringExtra(Constants.KEY_IMAGE_URL);
        guestHouseImage = findViewById(R.id.guestHouseImage);
        mapNavigate = findViewById(R.id.mapNavigate);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        context = this;
        supports = new Supports(context);
        preferencesManager = new PreferencesManager(context);
        guestHouseName = findViewById(R.id.guestHouseName);
        guestHouseCapacity = findViewById(R.id.guestHouseCapacity);
        guestHousePopulation = findViewById(R.id.guestHousePopulation);
        guestHouseAvailability = findViewById(R.id.guestHouseAvailability);
        monthList = Arrays.asList(getResources().getStringArray(R.array.month_array));
        yearList = yearList();
        typeList = Arrays.asList(getResources().getStringArray(R.array.type_array));
        guestHouseApplicationList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context);
    }

    private List<String> yearList() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<String> yearList = new ArrayList<>();
        yearList.add("Select Year");
        for (int i = year + 1; i >= 2022; i--) {
            yearList.add(String.valueOf(i));
        }
        return yearList;
    }

    private void setListeners() {
        setMonthSpinner();
        setYearSpinner();
        setTypeSpinner();

        Picasso.get().load(imageUrl).into(guestHouseImage);
        guestHouseName.setText(name);

        mapNavigate.setOnClickListener(v -> {
            Intent navigateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
            startActivity(navigateIntent);
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectedData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectedData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setMonthSpinner() {
        monthAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout, monthList);
        monthSpinner.setAdapter(monthAdapter);
    }

    private void setYearSpinner() {
        yearAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout, yearList);
        yearSpinner.setAdapter(yearAdapter);
    }

    private void setTypeSpinner() {
        typeAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout, typeList);
        typeSpinner.setAdapter(typeAdapter);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name + " " + getString(R.string.guest_house));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void getGuestHouseInfo() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handleGuestHouseInfo, error -> progressBar.setVisibility(View.GONE)) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_GUEST_HOUSE_INFO);
                params.put(Constants.KEY_ID, String.valueOf(id));
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void handleGuestHouseInfo(String response) {
        progressBar.setVisibility(View.GONE);

        try {
            JSONObject jsonObject = new JSONObject(response);
            guestHouseCapacity.setText(String.valueOf(jsonObject.get(Constants.KEY_CAPACITY)));
            guestHousePopulation.setText(String.valueOf(jsonObject.get(Constants.KEY_POPULATION)));
            guestHouseAvailability.setText(String.valueOf(jsonObject.get(Constants.KEY_AVAILABILITY)));
        } catch (Exception ignored) {

        }
    }

    private void setInitial() {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        monthSpinner.setSelection(month + 1);

        yearSpinner.setSelection(2);

    }

    private void getSelectedData() {
        int yearPosition = yearSpinner.getSelectedItemPosition();
        int year = 2024;
        if (yearPosition > 0) {
            year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
        } else {
            return;
        }

        int month = monthSpinner.getSelectedItemPosition();

        if (month == 0) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        fromTime = calendar.getTime().getTime();
        calendar.set(Calendar.MONTH, month + 1);
        calendar.set(Calendar.DAY_OF_MONTH, -1);
        toTime = calendar.getTime().getTime();

        getGuestHouseData();
    }

    private void getGuestHouseData() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handleGuestHouseData, error -> progressBar.setVisibility(View.GONE)){
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GUEST_HOUSE_DATA);
                    map.put(Constants.KEY_ID, String.valueOf(id));
                    map.put(Constants.KEY_FROM_TIME, String.valueOf(fromTime));
                    map.put(Constants.KEY_TO_TIME, String.valueOf(toTime));
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (Exception ignored) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void handleGuestHouseData(String response) {
        progressBar.setVisibility(View.GONE);
    }

}