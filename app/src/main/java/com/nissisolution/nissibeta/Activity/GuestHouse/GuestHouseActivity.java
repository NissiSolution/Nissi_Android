package com.nissisolution.nissibeta.Activity.GuestHouse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Adapter.GuestHouse.GuestHouseAdapter;
import com.nissisolution.nissibeta.Classes.GuestHouse;
import com.nissisolution.nissibeta.Classes.SingleGuestHouse;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Supports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuestHouseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView licence;
    private List<GuestHouse> guest_house;
    private List<SingleGuestHouse> single_house;
    private List<String> list_1, list_2;
    private LinearLayoutManager layoutManager;
    private Context context;
    private GuestHouseAdapter guest_house_adapter;
    private ProgressBar progressBar;
    private Supports supports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_house);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        declare_item();
        set_listeners();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.guest_house));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        progressBar.setVisibility(View.VISIBLE);
        send_request();
    }


    private void declare_item() {
        toolbar = findViewById(R.id.layout);
        recyclerView = findViewById(R.id.recyclerView);
        licence = findViewById(R.id.licence);
        progressBar = findViewById(R.id.progressBar);
        context = GuestHouseActivity.this;

        supports = new Supports(context);

        single_house = new ArrayList<>();
        guest_house = new ArrayList<>();
    }

    private void set_listeners() {
        supports.licence(licence);
    }

    private void send_request() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_GUESTHOUSE_LINK,
                this::handle_response,
                error -> {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_GUEST_HOUSE);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void handle_response(String response) {
        if (!response.equals(Constants.KEY_FAILURE)) {
            list_1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_1));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(list_1);
            }

            for (int i = 1; i < list_1.size(); i++) {
                list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
                SingleGuestHouse sgh = new SingleGuestHouse(Integer.parseInt(list_2.get(0)), list_2.get(1),
                        list_2.get(2), list_2.get(3), Integer.parseInt(list_2.get(4)));
                single_house.add(sgh);
            }

            if ((single_house.size()%2) == 0) {
                for (int i = 0; i < ((single_house.size())/2); i++) {
                    int id_1 = 2 * i;
                    int id_2 = id_1 + 1;
                    GuestHouse gh = new GuestHouse(single_house.get(id_1), single_house.get(id_2), 2);
                    guest_house.add(gh);
                }
            } else {
                for (int i = 0; i < ((single_house.size()+1)/2); i++) {
                    int id_1 = 2 * i;
                    int id_2 = id_1 + 1;
                    if (i == (list_1.size()-1)/2) {
                        GuestHouse gh = new GuestHouse(single_house.get(id_1), null, 1);
                        guest_house.add(gh);
                    } else {
                        GuestHouse gh = new GuestHouse(single_house.get(id_1), single_house.get(id_2), 2);
                        guest_house.add(gh);
                    }
                }
            }
        }
        set_recycler();
    }

    private void set_recycler() {
        progressBar.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        guest_house_adapter = new GuestHouseAdapter(context, guest_house);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(guest_house_adapter);
    }

}