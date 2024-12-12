package com.nissisolution.nissibeta.Activity.SosSupport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Adapter.SupportSos.SupportSosAdapter;
import com.nissisolution.nissibeta.Classes.SupportSos;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SosActivity extends AppCompatActivity {

    private TextView licence, error;
    private RecyclerView recyclerView;
    private Context context;
    private SupportSosAdapter support_sos_adapter;
    private List<SupportSos> sosList;
    private LinearLayoutManager layoutManager;
    private List<String> list_1, list_2;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.sos));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        licence = findViewById(R.id.licence);

        licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.KEY_NISSI_CONNECT));
                startActivity(intent);
            }
        });

        declare_item();
        set_request();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void declare_item() {
        error = findViewById(R.id.error);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        context = SosActivity.this;
        sosList = new ArrayList<>();
    }

    private void set_request() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_GUESTHOUSE_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handle_response(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_SOS_MESSAGE);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_response(String response) {
        list_1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_1));

        Collections.sort(list_1);

        for (int i = 1; i < list_1.size(); i++) {
            list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
            SupportSos sos = new SupportSos(Integer.parseInt(list_2.get(0)), list_2.get(1), list_2.get(2), list_2.get(3), list_2.get(4));
            sosList.add(sos);
        }

        set_recycler();
    }

    private void set_recycler(){
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.hasFixedSize();
        support_sos_adapter = new SupportSosAdapter(Constants.KEY_SOS_MESSAGE, context, sosList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(support_sos_adapter);
        progressBar.setVisibility(View.GONE);
        if (sosList.size() > 1) {
            error.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }


}