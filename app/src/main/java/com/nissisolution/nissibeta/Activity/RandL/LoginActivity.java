package com.nissisolution.nissibeta.Activity.RandL;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.nissisolution.nissibeta.Activity.Others.MainPageActivity;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Supports;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextInputEditText email, password;
    private TextView licence;
    private String check_in_url, email_, password_;
    private PreferencesManager preferencesManager;
    private ProgressBar progressBar;
    private Supports supports;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_login);

        declare_item();
        set_listeners();
        check_location_permission();
    }

    private void declare_item() {
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        licence = findViewById(R.id.licence);
        preferencesManager = new PreferencesManager(this);
        progressBar = findViewById(R.id.progressBar);

        check_in_url = Constants.KEY_DATABASE_LINK;
        context = this;
        supports = new Supports(context);
    }

    private void set_listeners() {
        login.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            email_ = email.getText().toString().trim();
            password_ = password.getText().toString().trim();
            if (!location_permission()){
                check_location_permission();
                supports.create_short_toast(Constants.KEY_LOCATION_PERMISSION_REQUIRED);
                progressBar.setVisibility(View.GONE);
            } else if (email_.isEmpty()){
                email.setError(Constants.KEY_REQUIRED);
                email.requestFocus();
                progressBar.setVisibility(View.GONE);
            }else if (password_.isEmpty()){
                password.setError(Constants.KEY_REQUIRED);
                password.requestFocus();
                progressBar.setVisibility(View.GONE);
            }else {
                post_method();
            }

        });

        supports.licence(licence);
    }

    private void post_method() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_in_url,
                response -> {
                    String[] sp_res = response.split(Constants.KEY_HYPHEN);
                    if (sp_res[0].equals(Constants.KEY_EMPTY) ){
                        supports.create_short_toast(Constants.KEY_EMAIL_ERROR);
                        progressBar.setVisibility(View.GONE);
                    }else if (sp_res[0].trim().equals(Constants.KEY_ERROR)){
                        supports.create_short_toast(Constants.KEY_PASSWORD_ERROR);
                        progressBar.setVisibility(View.GONE);
                    }else{
                        preferencesManager.putString(Constants.KEY_STAFF_ID, sp_res[0].trim());
                        preferencesManager.putString(Constants.KEY_PASSWORD, password_);
                        supports.start_activity(MainPageActivity.class);
                        finish();
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> Toast.makeText(LoginActivity.this, Constants.KEY_SERVER_ERROR, Toast.LENGTH_SHORT).show()){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_AUTH);
                params.put(Constants.KEY_EMAIL, email_);
                params.put(Constants.KEY_PASSWORD, password_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }


    private void check_location_permission() {
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    private boolean location_permission() {
        return ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

}