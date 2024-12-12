package com.nissisolution.nissibeta.Activity.GuestHouse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.nissisolution.nissibeta.Adapter.GuestHouseAvailability.GuestHouseAvailabilityAdapter;
import com.nissisolution.nissibeta.Classes.GuestHouseAvailability;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.Supports.Supports;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestHouseDetailsActivity extends AppCompatActivity {

    private int position, id;
    private String name, mapUrl, imageUrl;
    protected ShapeableImageView guestHouseImage, mapNavigate;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Context context;
    private Supports supports;
    private PreferencesManager preferencesManager;
    private TextView guestHouseName, guestHouseCapacity, guestHousePopulation, guestHouseAvailability;
    private List<GuestHouseAvailability> guestHouseAvailabilityList;
    private LinearLayoutManager layoutManager;
    private GuestHouseAvailabilityAdapter guestHouseAvailabilityAdapter;
    private FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guest_house_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        declareItem();
        setToolbar();
        setListeners();

        guestHouseAvailabilityList.add(new GuestHouseAvailability(4, 0, 0, 0, 0));
        guestHouseAvailabilityList.add(new GuestHouseAvailability(3, 1, 2, 0, 0));
        guestHouseAvailabilityList.add(new GuestHouseAvailability(2, 1, 2, 0, 0));
        guestHouseAvailabilityList.add(new GuestHouseAvailability(1, 1, 0, 0, 0));
        setRecyclerView();
        progressBar.setVisibility(View.GONE);
        getGuestHouseInfo();
        checkPermission();
    }

    private void declareItem() {
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
        guestHouseAvailabilityList = new ArrayList<>();
        floatingButton = findViewById(R.id.floatingButton);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name + " " + getString(R.string.guest_house));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void setListeners() {
        Picasso.get().load(imageUrl).into(guestHouseImage);
        guestHouseName.setText(name);

        mapNavigate.setOnClickListener(v -> {
            Intent navigateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
            startActivity(navigateIntent);
        });

        floatingButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, GuestHouseApplicationActivity.class);
            intent.putExtra(Constants.KEY_ID, id);
            intent.putExtra(Constants.KEY_POSITION, position);
            intent.putExtra(Constants.KEY_NAME, name);
            intent.putExtra(Constants.KEY_MAP_URL, mapUrl);
            intent.putExtra(Constants.KEY_IMAGE_URL, imageUrl);
            startActivity(intent);
        });

    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        guestHouseAvailabilityAdapter = new GuestHouseAvailabilityAdapter(context, guestHouseAvailabilityList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(guestHouseAvailabilityAdapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

    private void checkPermission() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                    this::handlePermission, error -> progressBar.setVisibility(View.GONE)) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_CHECK_GUEST_HOUSE_PERMISSION);
                    map.put(Constants.KEY_STAFF_ID, preferencesManager.getString(Constants.KEY_STAFF_ID));
                    map.put(Constants.KEY_ID, String.valueOf(id));
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        } catch (Exception ignored) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void handlePermission(String response) {
        progressBar.setVisibility(View.GONE);
        if (response.equals(Constants.KEY_ADMIN)) {
            floatingButton.setVisibility(View.VISIBLE);
        } else {
            floatingButton.setVisibility(View.GONE);
        }
    }

}