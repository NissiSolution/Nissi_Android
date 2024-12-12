package com.nissisolution.nissibeta.Activity.SosSupport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Supports;

public class FullMessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title, author, message, datetime, license;
    private Intent get_intent;
    private String s_title, s_timestamp, s_author, s_message, s_toolbar;
    private Supports supports;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_message);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        declare_item();
        get_intent();
        set_listeners();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(s_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        set_data();

    }

    private void declare_item() {
        toolbar = findViewById(R.id.layout);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        message = findViewById(R.id.message);
        license = findViewById(R.id.licence);
        datetime = findViewById(R.id.date_time);

        context = FullMessageActivity.this;

        supports = new Supports(context);
    }

    private void set_listeners() {
        supports.licence(license);
    }

    private void get_intent() {
        get_intent = getIntent();
        s_title = get_intent.getStringExtra(Constants.KEY_TITLE);
        s_timestamp = get_intent.getStringExtra(Constants.KEY_TIMESTAMP);
        s_author = get_intent.getStringExtra(Constants.KEY_AUTHOR);
        s_message = get_intent.getStringExtra(Constants.KEY_MESSAGE);
        s_toolbar = get_intent.getStringExtra(Constants.KEY_TOOLBAR);
    }

    private void set_data() {
        title.setText(s_title);
        author.setText(s_author);
        message.setText("   " + s_message);
        datetime.setText(s_timestamp);
    }

}