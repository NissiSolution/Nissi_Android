package com.nissisolution.nissibeta.Activity.Others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.nissisolution.nissibeta.Activity.RandL.LoginActivity;
import com.nissisolution.nissibeta.Supports.PreferencesManager;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Supports;

public class MainActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager, preferencesManager2;
    private Supports supports;
    private String staff_id, password;
    private TextView licence;
    private Context context;
    private boolean isNotDone = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        declare_item();

//        preferencesManager.clear();
//        preferencesManager.putString(Constants.KEY_STAFF_ID, "270");
//        preferencesManager.putString(Constants.KEY_PASSWORD, "123456");
        staff_id = preferencesManager.getString(Constants.KEY_STAFF_ID);
        password = preferencesManager.getString(Constants.KEY_PASSWORD);

        new_handler();
        setNotifications();
    }

    private void declare_item() {
        context = this;
        preferencesManager = new PreferencesManager(context);
        preferencesManager2 = new PreferencesManager(context, Constants.KEY_NOTIFICATION);
        supports = new Supports(context);
        licence = findViewById(R.id.licence);
        supports.licence(licence);
    }

    private void new_handler() {
        new Handler().postDelayed(() -> {
            if (isNotDone) {
                Intent intent;
                if (staff_id == null || password == null) {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, MainPageActivity.class);
                }
                startActivity(intent);
                finish();
                isNotDone = false;
            }
        },1000);
    }

    private void setNotifications() {
        if (!preferencesManager2.getBoolean(Constants.KEY_NOTIFICATION)) {
            preferencesManager2.putBoolean(Constants.KEY_ALLOW_NOTIFICATIONS, true);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll();
        }
    }

}