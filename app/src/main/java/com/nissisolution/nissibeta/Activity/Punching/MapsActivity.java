package com.nissisolution.nissibeta.Activity.Punching;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.databinding.ActivityMapsBinding;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng chennai, bangalore, karol_bagh, gurgaon, hyderabad, kolkata, valliyur, nasik;
    private PreferencesManager preferencesManager;
    private String check_in_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferencesManager = new PreferencesManager(this);
        check_in_url = Constants.KEY_DATABASE_LINK;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                show_my_location();
            }
        }, 1000);

        upload_times(preferencesManager.getString(Constants.KEY_STAFF_ID));
        // show_all_location();
    }

    private void show_my_location() {
        try {
            Location location = mMap.getMyLocation();
            LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(17).bearing(0).tilt(30).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
        }catch (Exception ignored) {

        }
    }

    private void show_all_location() {
        chennai = new LatLng(13.144776454789284, 80.13609325640233);
        bangalore = new LatLng(12.92892894401229, 77.51021978365867);
        karol_bagh = new LatLng(28.656114, 77.199136);
        gurgaon = new LatLng(28.423583, 77.084722);
        hyderabad = new LatLng(17.48324080878861, 78.55466569091122);
        kolkata = new LatLng(22.59912975839572, 88.4026087758687);
        nasik = new LatLng(19.950869403009133, 73.75709886008217);
        valliyur = new LatLng(8.394306, 77.609056);

        mMap.addMarker(new MarkerOptions().position(chennai).title(Constants.KEY_CHENNAI)
                    .snippet(Constants.KEY_CHENNAI_GUEST_HOUSE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions().position(bangalore).title(Constants.KEY_BANGALORE)
                .snippet(Constants.KEY_BANGALORE_GUEST_HOUSE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions().position(karol_bagh).title(Constants.KEY_KAROL_BAGH)
                .snippet(Constants.KEY_KAROL_BAGH_GUEST_HOUSE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions().position(gurgaon).title(Constants.KEY_GURGAON)
                .snippet(Constants.KEY_GURGAON_GUEST_HOUSE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions().position(hyderabad).title(Constants.KEY_HYDERABAD)
                .snippet(Constants.KEY_HYDERABAD_GUEST_HOUSE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions().position(kolkata).title(Constants.KEY_KOLKATA)
                .snippet(Constants.KEY_KOLKATA_GUEST_HOUSE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions().position(nasik).title(Constants.KEY_NASIK)
                .snippet(Constants.KEY_NASIK_GUEST_HOUSE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions().position(valliyur).title(Constants.KEY_VALLIYUR)
                .snippet(Constants.KEY_VALLIYUR_GUEST_HOUSE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }

    private void upload_times(String s_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_in_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_SET_TIMES);
                params.put(Constants.KEY_STAFF_ID, s_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
        requestQueue.add(stringRequest);
    }

}