package com.nissisolution.nissibeta.Activity.Locations;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.databinding.ActivityUniqueLocationBinding;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class UniqueLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityUniqueLocationBinding binding;
    private String data;
    private Intent get_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUniqueLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void get_data() {
        get_intent = getIntent();
        data = get_intent.getStringExtra(Constants.KEY_DATA);
        List<String> list1 = Arrays.asList(data.split(Constants.KEY_SPLITTER_2));
        map_data(list1.get(0), get_date_3(list1.get(3)), Double.parseDouble(list1.get(1)), Double.parseDouble(list1.get(2)), Integer.parseInt(list1.get(4)));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        get_data();
    }

    private void map_data(String name, String time, double longitude, double latitude, int type) {
        LatLng location = new LatLng(latitude, longitude);

        if (type == 1) {
            mMap.addMarker(new MarkerOptions().position(location).title(name).
                    snippet(Constants.KEY_PUNCHED_IN_AT + time).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
        } else {
            mMap.addMarker(new MarkerOptions().position(location).title(name).
                    snippet(Constants.KEY_PUNCHED_OUT_AT + time).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    @SuppressLint("SimpleDateFormat")
    private String get_date_3(String datetime) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        try {
            return format2.format(format1.parse(datetime));
        } catch (Exception ignored) {
            return "Unknown";
        }
    }
}