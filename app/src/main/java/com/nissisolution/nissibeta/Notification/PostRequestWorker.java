package com.nissisolution.nissibeta.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.PreferencesManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostRequestWorker extends Worker {

    private Context context;
    private PreferencesManager preferencesManager;

    public PostRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        preferencesManager = new PreferencesManager(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Your logic to perform the POST request
        // For example, using HttpURLConnection or OkHttp library

        // Sample code (not an actual POST request implementation):
        boolean success = performPostRequest();

        if (success) {
            return Result.success();
        } else {
            return Result.retry();
        }
    }

    private boolean performPostRequest() {
        // Implement your POST request here
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    Intent intent = new Intent(context, NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> {
                    long timeNow = new Date().getTime();

                    preferencesManager.putString(Constants.KEY_LAST_CHECKED_IN_TIME, String.valueOf(timeNow));
                    preferencesManager.putBoolean(Constants.KEY_LAST_CHECKED_IN, true);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    long triggerTime = timeNow + 1 * 60 * 1000;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_NOTIFICATION);
                params.put(Constants.KEY_STAFF_ID, new PreferencesManager(context).getString(Constants.KEY_STAFF_ID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        return true; // Return true if successful, false otherwise
    }
}

