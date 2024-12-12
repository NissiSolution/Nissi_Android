package com.nissisolution.nissibeta.Supports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class Supports {

    private Context context;

    public Supports(Context context) {
        this.context = context;
    }

    public void create_short_toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void create_long_toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void licence(View element) {
        element.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Constants.KEY_NISSI_CONNECT));
            context.startActivity(intent);
        });
    }

    public void start_activity(Class s_class) {
        Intent intent = new Intent(context, s_class);
        context.startActivity(intent);
    }

    public void start_activity_listener(View element, Class s_class) {
        element.setOnClickListener(view -> {
            Intent intent = new Intent(context, s_class);
            context.startActivity(intent);
        });
    }

    public int get_current_status(String the_data) {
        try {
            List<String> list_1 = Arrays.asList(the_data.split(Constants.KEY_SPLITTER_1));
            int the_status = 0;
            for (int i = 1; i < list_1.size(); i++) {
                List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
                if (list_2.get(1).equals(Constants.KEY_1)) {
                    return 1;
                } else if (list_2.get(1).equals(Constants.KEY_2)) {
                    the_status = 2;
                }
            }
            return the_status;
        } catch (Exception e) {
            return 0;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public long get_timestamp(int year, int month, int day) {
        String d, m, y;
        if (month < 10) {
            m = "0" + month;
        } else {
            m = String.valueOf(month);
        }

        if (day < 10) {
            d = "0" + day;
        } else {
            d = String.valueOf(day);
        }

        y = String.valueOf(year);

        String the_date = d + "-" + m + "-" + y;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return format.parse(the_date).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public void edit_text_watcher(TextInputEditText element) {
        element.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                element.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                element.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                element.setError(null);
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public String get_last_checked_time_text(String the_value) {
        // alert_dialog(the_value);
        List<String> list_1 = Arrays.asList(the_value.split(Constants.KEY_SPLITTER_1));
        if (list_1.size() > 1) {
            List<String> list_2 = Arrays.asList(list_1.get(list_1.size() - 1).split(Constants.KEY_SPLITTER_2));
            SimpleDateFormat format_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format_2 = new SimpleDateFormat("hh:mm a");
            try {
                if (list_2.get(1).equals(Constants.KEY_1)) {
                    return format_2.format(format_1.parse(list_2.get(0)));
                } else {
                    return null;
                }
            } catch (Exception ignored) {
                return null;
            }
        } else {
            return null;
        }
    }

    public double get_last_checked_time_value(String the_value) {
        List<String> list_1 = Arrays.asList(the_value.split(Constants.KEY_SPLITTER_1));
        String date_1 = null;
        String date_2 = null;
        String date_1_type = null;
        String date_2_type = null;
        double the_duration = 0;

        for (int i = 1; i < list_1.size(); i++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
            if (date_1 == null) {
                if (list_2.get(1).equals(Constants.KEY_1)) {
                    date_1 = list_1.get(i);
                    date_1_type = list_2.get(1);
                }
            } else {
                date_2 = list_1.get(i);
                date_2_type = list_2.get(1);
                if (date_1_type.equals(Constants.KEY_1)) {
                    if (!date_1_type.equals(date_2_type)) {
                        the_duration = the_duration + get_date(date_1, date_2);
                        date_1 = date_2;
                        date_1_type = date_2_type;
                    }
                } else {
                    date_1 = date_2;
                    date_1_type = date_2_type;
                }
            }
        }
        return the_duration / (60 * 60 * 1000);
    }


    public double get_date(String the_date_1, String the_date_2) {
        List<String> list_1 = Arrays.asList(the_date_1.split(Constants.KEY_SPLITTER_2));
        List<String> list_2 = Arrays.asList(the_date_2.split(Constants.KEY_SPLITTER_2));

        if (list_1.get(1).equals(list_2.get(1))) {
            return 0;
        } else {
            if (list_1.get(1).equals(Constants.KEY_1)) {
                return get_duration(the_date_1, the_date_2);
            } else {
                return 0;
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public double get_duration(String date_1, String date_2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            double timestamp_1 = format.parse(date_1).getTime();
            double timestamp_2 = format.parse(date_2).getTime();
            return timestamp_2 - timestamp_1;
        } catch (Exception ignored) {
            return 0;
        }
    }

    public void alert_dialog(String the_message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(the_message).setTitle("Message")
                .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create();
        builder.show();
    }

}
