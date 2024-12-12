package com.nissisolution.nissibeta.CustomView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.imageview.ShapeableImageView;
import com.nissisolution.nissibeta.Adapter.CheckInOut.CheckInOutAdapter2;
import com.nissisolution.nissibeta.Adapter.Display.DisplayAdapter;
import com.nissisolution.nissibeta.R;
import com.nissisolution.nissibeta.Supports.Constants;
import com.nissisolution.nissibeta.Supports.Supports;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCalendar extends LinearLayout {

    public Context context;
    private LinearLayout header_layout, month_layout, week_layout, layout_1, layout_2, layout_3, layout_4, layout_5, layout_6;
    private ShapeableImageView previous, next;
    private TextView mont, yea, sun, mon, tue, wed, thu, fri, sat, d1, d2, d3, d4, d5, d6, d7, d8,
            d9, d10, d11, d12, d13, d14, d15, d16, d17, d18, d19, d20, d21, d22, d23, d24, d25, d26,
            d27, d28, d29, d30, d31, d32, d33, d34, d35, d36, d37, d38, d39, d40, d41, d42;
    private static final String KEY_SUNDAY = "Sun";
    private static final String KEY_MONDAY = "Mon";
    private static final String KEY_TUESDAY = "Tue";
    private static final String KEY_WEDNESDAY = "Wed";
    private static final String KEY_THURSDAY = "Thu";
    private static final String KEY_FRIDAY = "Fri";
    private static final String KEY_SATURDAY = "Sat";
    private static final String[] month_list = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private List<String> dates_list = new ArrayList<>();
    private List<TextView> text_date_list, text_sunday_list, text_week_day_list, text_saturday_list, text_week_head_list;
    private List<LinearLayout> layout_date_list;
    private List<CalenderClass> calender_list;
    private final int signature = Color.parseColor("#03A9F4");
    private final int white = Color.parseColor("#FFFFFFFF");
    private final Drawable round_drawable = getResources().getDrawable(R.drawable.calender_round);
    private final Drawable curved_drawable = getResources().getDrawable(R.drawable.calender_corner);
    private String current_month, current_year;
    private List<CustomView> customViewList;
    private List<String> fetchedMonth;
    private ProgressBar progressBar;
    private String staffId;

    @SuppressLint("CustomViewStyleable") @SuppressWarnings("ResourceAsColor")
    public CustomCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorOptionsView, 0, 0);
        String titleText = a.getString(R.styleable.ColorOptionsView_titleText);

        int valueColor = a.getColor(R.styleable.ColorOptionsView_valueColor, android.R.color.holo_blue_light);
        a.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_custom_calender, this, true);
        declareItem();
        setListeners();
        setText();
    }

    private void declareItem() {
        header_layout = (LinearLayout) getChildAt(0);
        previous = (ShapeableImageView) header_layout.getChildAt(0);
        month_layout = (LinearLayout) header_layout.getChildAt(1);
        mont = (TextView) month_layout.getChildAt(0);
        yea = (TextView) month_layout.getChildAt(1);
        next = (ShapeableImageView) header_layout.getChildAt(2);

        week_layout = (LinearLayout) getChildAt(1);
        sun = (TextView) week_layout.getChildAt(0);
        mon = (TextView) week_layout.getChildAt(1);
        tue = (TextView) week_layout.getChildAt(2);
        wed = (TextView) week_layout.getChildAt(3);
        thu = (TextView) week_layout.getChildAt(4);
        fri = (TextView) week_layout.getChildAt(5);
        sat = (TextView) week_layout.getChildAt(6);

        layout_1 = (LinearLayout) getChildAt(2);
        d1 = (TextView) layout_1.getChildAt(0);
        d2 = (TextView) layout_1.getChildAt(1);
        d3 = (TextView) layout_1.getChildAt(2);
        d4 = (TextView) layout_1.getChildAt(3);
        d5 = (TextView) layout_1.getChildAt(4);
        d6 = (TextView) layout_1.getChildAt(5);
        d7 = (TextView) layout_1.getChildAt(6);

        layout_2 = (LinearLayout) getChildAt(3);
        d8 = (TextView) layout_2.getChildAt(0);
        d9 = (TextView) layout_2.getChildAt(1);
        d10 = (TextView) layout_2.getChildAt(2);
        d11 = (TextView) layout_2.getChildAt(3);
        d12 = (TextView) layout_2.getChildAt(4);
        d13 = (TextView) layout_2.getChildAt(5);
        d14 = (TextView) layout_2.getChildAt(6);

        layout_3 = (LinearLayout) getChildAt(4);
        d15 = (TextView) layout_3.getChildAt(0);
        d16 = (TextView) layout_3.getChildAt(1);
        d17 = (TextView) layout_3.getChildAt(2);
        d18 = (TextView) layout_3.getChildAt(3);
        d19 = (TextView) layout_3.getChildAt(4);
        d20 = (TextView) layout_3.getChildAt(5);
        d21 = (TextView) layout_3.getChildAt(6);

        layout_4 = (LinearLayout) getChildAt(5);
        d22 = (TextView) layout_4.getChildAt(0);
        d23 = (TextView) layout_4.getChildAt(1);
        d24 = (TextView) layout_4.getChildAt(2);
        d25 = (TextView) layout_4.getChildAt(3);
        d26 = (TextView) layout_4.getChildAt(4);
        d27 = (TextView) layout_4.getChildAt(5);
        d28 = (TextView) layout_4.getChildAt(6);

        layout_5 = (LinearLayout) getChildAt(6);
        d29 = (TextView) layout_5.getChildAt(0);
        d30 = (TextView) layout_5.getChildAt(1);
        d31 = (TextView) layout_5.getChildAt(2);
        d32 = (TextView) layout_5.getChildAt(3);
        d33 = (TextView) layout_5.getChildAt(4);
        d34 = (TextView) layout_5.getChildAt(5);
        d35 = (TextView) layout_5.getChildAt(6);

        layout_6 = (LinearLayout) getChildAt(7);
        d36 = (TextView) layout_6.getChildAt(0);
        d37 = (TextView) layout_6.getChildAt(1);
        d38 = (TextView) layout_6.getChildAt(2);
        d39 = (TextView) layout_6.getChildAt(3);
        d40 = (TextView) layout_6.getChildAt(4);
        d41 = (TextView) layout_6.getChildAt(5);
        d42 = (TextView) layout_6.getChildAt(6);

        TextView[] the_list_1 = {d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15, d16,
                d17, d18, d19, d20, d21, d22, d23, d24, d25, d26, d27, d28, d29, d30, d31, d32, d33,
                d34, d35, d36, d37, d38, d39, d40, d41, d42};
        text_date_list = Arrays.asList(the_list_1);

        LinearLayout[] the_list_2 = {layout_1, layout_2, layout_3, layout_4, layout_5, layout_6};
        layout_date_list = Arrays.asList(the_list_2);

        TextView[] the_list_3 = {sun, d1, d8, d15, d22, d29, d36};
        text_sunday_list = Arrays.asList(the_list_3);

        TextView[] the_list_4 = {sat, d7, d14, d21, d28, d35, d42};
        text_saturday_list = Arrays.asList(the_list_4);

        TextView[] the_list_5 = {mon, tue, wed, thu, fri, d2, d3, d4, d5, d6, d9, d10, d11, d12, d13,
                d16, d17, d18, d19, d20, d23, d24, d25, d26, d27, d30, d31, d32, d33, d34, d37,
                d38, d39, d40, d41};
        text_week_day_list = Arrays.asList(the_list_5);

        TextView[] the_list_6 = {mon, tue, wed, thu, fri};
        text_week_head_list = Arrays.asList(the_list_6);
        calender_list = new ArrayList<>();
        customViewList = new ArrayList<>();
    }


    private void setListeners() {
        previous.setOnClickListener(v -> {
            int m = Integer.parseInt(current_month);
            int y = Integer.parseInt(current_year);
            if (m == 1) {
                setMonthYear(12, y-1);
            } else {
                setMonthYear(m-1, y);
            }
            hideHighlighter();
            setHighlighter2();
            displayContent();
            if ((Calendar.getInstance().get(Calendar.MONTH) + 1) > Integer.parseInt(current_month) &&
                    (Calendar.getInstance().get(Calendar.YEAR) == Integer.parseInt(current_year))) {
                getMyDetailsInfo();
            } else if ((Calendar.getInstance().get(Calendar.YEAR) > Integer.parseInt(current_year))) {
                getMyDetailsInfo();
            }
        });

        next.setOnClickListener(v -> {
            int m = Integer.parseInt(current_month);
            int y = Integer.parseInt(current_year);
            if (m == 12) {
                setMonthYear(1, y+1);
            } else {
                setMonthYear(m+1, y);
            }
            hideHighlighter();
            setHighlighter2();
            displayContent();
            if ((Calendar.getInstance().get(Calendar.MONTH) + 1) > Integer.parseInt(current_month) &&
                    (Calendar.getInstance().get(Calendar.YEAR) == Integer.parseInt(current_year))) {
                getMyDetailsInfo();
            } else if ((Calendar.getInstance().get(Calendar.YEAR) > Integer.parseInt(current_year))) {
                getMyDetailsInfo();
            }
        });
    }

    public void setData(Context theContext, ProgressBar theProgressBar, String theStaffId) {
        context = theContext;
        progressBar = theProgressBar;
        staffId = theStaffId;
    }

    private void setText() {
        setMonthYear(11, 2000);

        sun.setText(KEY_SUNDAY);
        mon.setText(KEY_MONDAY);
        tue.setText(KEY_TUESDAY);
        wed.setText(KEY_WEDNESDAY);
        thu.setText(KEY_THURSDAY);
        fri.setText(KEY_FRIDAY);
        sat.setText(KEY_SATURDAY);
    }

    public void setDatesBackground(int color) {
        for (LinearLayout element:layout_date_list) {
            element.setBackgroundColor(getResources().getColor(color));
        }
    }

    private void addCalenderList(long timestamp, Drawable drawable, int backgroundColor, int textColor) {
        CalenderClass calenderClass = new CalenderClass(timestamp, drawable, backgroundColor, textColor);
        calender_list.add(calenderClass);
    }

    public void setRoundBackground(long timestamp) {
        addCalenderList(timestamp, round_drawable, signature, white);
    }

    public void setRoundBackground(int textColor, long timestamp) {
        addCalenderList(timestamp, round_drawable, signature, textColor);
    }

    public void setRoundBackground(long timestamp, int backgroundColor) {
        addCalenderList(timestamp, round_drawable, backgroundColor, white);
    }

    public void setRoundBackground(int textColor, long timestamp, int backgroundColor) {
        addCalenderList(timestamp, round_drawable, backgroundColor, textColor);
    }

    public void setCurvedBackground(long timestamp) {
        addCalenderList(timestamp, curved_drawable, signature, white);
    }

    public void setCurvedBackground(int textColor, long timestamp) {
        addCalenderList(timestamp, curved_drawable, signature, textColor);
    }

    public void setCurvedBackground(long timestamp, int backgroundColor) {
        addCalenderList(timestamp, curved_drawable, backgroundColor, white);
    }

    public void setCurvedBackground(int textColor, long timestamp, int backgroundColor) {
        addCalenderList(timestamp, curved_drawable, backgroundColor, textColor);
    }

    public void setHeaderBackground(int color) {
        header_layout.setBackgroundColor(color);
        week_layout.setBackgroundColor(color);
    }

    public void setSundayTextColor(int color) {
        for (TextView element:text_sunday_list) {
            element.setTextColor(color);
        }
    }

    public void setDaysTextColor(int color) {
        for (TextView element:text_week_day_list) {
            element.setTextColor(color);
        }
    }

    public void setSaturdayTextColor(int color) {
        for (TextView element:text_sunday_list) {
            element.setTextColor(color);
        }
    }

    public void setHeaderTextColor(int color) {
        for (TextView element:text_week_head_list) {
            element.setTextColor(color);
        }
    }

    public void setMonthYear(int month, int year) {
        try {
            mont.setText(month_list[month - 1]);
        } catch (Exception ignored) {
            return;
        }

        yea.setText(String.valueOf(year));

        displayDates(month, year);
        current_month = getString(month);
        current_year = getString(year);
    }
    
    private String getString(int the_int) {
        if (the_int < 10) {
            return "0" + the_int;
        } else {
            return String.valueOf(the_int);
        }
    }
    
    private void displayDates() {
        for (int i = 0; i < dates_list.size(); i++) {
            for (CalenderClass calender: calender_list) {
                if (getDate(calender.timestamp).equals(dates_list.get(i) + "-" + current_month + "-" + current_year)) {
                    text_date_list.get(i).setTextColor(calender.textColor);
                    text_date_list.get(i).setBackground(calender.drawable);
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void displayDates(int month, int year) {
        try {
            Calendar c = Calendar.getInstance();
            YearMonth yearMonth;
            int end_date, day;
            String the_date, the_month;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                yearMonth = YearMonth.of(year, month);
                end_date = yearMonth.lengthOfMonth();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                the_date = "01";
                if (month < 10) {
                    the_month = "-0" + month + "-";
                } else {
                    the_month = "-" + month + "-";
                }

                Date date = format.parse(the_date + the_month + year);
                day = date.getDay();

                if (end_date == 31) {
                    layout_5.setVisibility(VISIBLE);
                    if (day == 6 || day == 5) {
                        layout_6.setVisibility(VISIBLE);
                    } else {
                        layout_6.setVisibility(GONE);
                    }
                } else if (end_date == 30) {
                    layout_5.setVisibility(VISIBLE);
                    if (day == 6) {
                        layout_6.setVisibility(VISIBLE);
                    } else {
                        layout_6.setVisibility(GONE);
                    }
                } else if (end_date == 28) {
                    if (day == 0) {
                        layout_5.setVisibility(GONE);
                        layout_6.setVisibility(GONE);
                    } else {
                        layout_5.setVisibility(VISIBLE);
                        layout_6.setVisibility(GONE);
                    }
                } else {
                    layout_5.setVisibility(VISIBLE);
                    layout_6.setVisibility(GONE);
                }
                setDisplay(day, end_date);
            }
        } catch (Exception ignored) {

        }
    }

    private void setDisplay(int day, int end_date) {
        dates_list.clear();
        int previous_date = 0;
        for (int i = 0; i < 42; i++) {
            if (i < day) {
                dates_list.add(null);
            } else if (i == 0) {
                dates_list.add("01");
                previous_date = 1;
            } else {
                int current_date = previous_date + 1;
                if (current_date <= end_date) {
                    if (current_date < 10) {
                        dates_list.add("0" + current_date);
                    } else {
                        dates_list.add(String.valueOf(current_date));
                    }
                } else {
                    dates_list.add(null);
                }
                previous_date++;
            }
        }
        for (int i=0; i < text_date_list.size(); i++) {
            text_date_list.get(i).setText(dates_list.get(i));
        }
    }

    public void setHighlighter(long timestamp, Drawable drawable, int color) {
        customViewList.add(new CustomView(timestamp, drawable, color));
        String day = getDay(timestamp);
        String month = getMonth(timestamp);
        String year = getYear(timestamp);
        if (current_month.equals(month) && current_year.equals(year)) {
            for (int i = 0; i < dates_list.size(); i++) {
                if (dates_list.get(i) != null && dates_list.get(i).equals(day)) {
                    text_date_list.get(i).setBackground(drawable);
                    text_date_list.get(i).setTextColor(color);
                    text_date_list.get(i).setTypeface(null, Typeface.BOLD);
                }
            }
        }
    }

    private void hideHighlighter() {
        Integer[] sun = {0, 7, 14, 21, 28, 35};
        List<Integer> sunList = Arrays.asList(sun);
        Integer[] sat = {6, 13, 20, 27, 34};
        List<Integer> satList = Arrays.asList(sat);
        for (int i = 0; i < dates_list.size(); i++) {
            text_date_list.get(i).setBackground(null);
            text_date_list.get(i).setTypeface(null, Typeface.NORMAL);
            if (sunList.contains(i)) {
                text_date_list.get(i).setTextColor(Color.parseColor("#FFFF0000"));
            } else if (satList.contains(i)) {
                text_date_list.get(i).setTextColor(Color.parseColor("#FF000000"));
            } else {
                text_date_list.get(i).setTextColor(Color.parseColor("#03A9F4"));
            }
        }
    }

    private void setHighlighter2() {
        for (CustomView customView : customViewList) {
            String day = getDay(customView.timestamp);
            String month = getMonth(customView.timestamp);
            String year = getYear(customView.timestamp);
            if (current_month.equals(month) && current_year.equals(year)) {
                for (int i = 0; i < dates_list.size(); i++) {
                    if (dates_list.get(i) != null && dates_list.get(i).equals(day)) {
                        text_date_list.get(i).setBackground(customView.drawable);
                        text_date_list.get(i).setTextColor(customView.color);
                        text_date_list.get(i).setTypeface(null, Typeface.BOLD);
                    }
                }
            }
        }
    }

    public void setHighlighter(long timestamp, Drawable drawable, int color, int pos) {
        String day = getDay(timestamp);
        for (int i=0; i < dates_list.size(); i++) {
            if (dates_list.get(i) != null && dates_list.get(i).equals(day)) {
                text_date_list.get(i).setBackground(drawable);
                //text_date_list.get(i).set
                text_date_list.get(i).setTextColor(color);
                text_date_list.get(i).setTypeface(null, Typeface.BOLD);;
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(new Date(timestamp));
    }

    @SuppressLint("SimpleDateFormat")
    private String getDay(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(new Date(timestamp));
    }

    @SuppressLint("SimpleDateFormat")
    private String getMonth(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(new Date(timestamp));
    }

    @SuppressLint("SimpleDateFormat")
    private String getYear(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(timestamp));
    }
    
    @SuppressLint("SimpleDateFormat")
    private long getTimestamp(String the_date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return format.parse(the_date).getTime();
        } catch (Exception ignored) {
            return 0;
        }
    }

    private long getTimestamp2(String the_date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(the_date).getTime();
        } catch (Exception ignored) {
            return 0;
        }
    }

    public void displayContent() {
        for (TextView text : text_date_list) {
            text.setOnClickListener(v -> {
                if (text.getText().toString().length() == 2) {
                    if (getTimestamp2(current_year + "-" + current_month + "-" + text.getText().toString()) < new Date().getTime()) {
                        display_content(context, progressBar, staffId, text.getText().toString());
                    }
                }
            });
        }
    }

    private void display_content(Context context, ProgressBar progressBar, String staffId, String d) {
        progressBar.setVisibility(VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                response -> {
                    show_recycler(context, response);
            progressBar.setVisibility(GONE);
                }, error -> {
            progressBar.setVisibility(GONE);
            Toast.makeText(context, Constants.KEY_SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_THE_INFORMATION);
                params.put(Constants.KEY_STAFF_ID, staffId);
                params.put(Constants.KEY_DATE, get_Date(d));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private String get_Date(String d) {
        return current_year + "-" + current_month + "-" + d;
    }

    private void show_recycler(Context context, String response) {
        List<String> data_1 = Arrays.asList(response.split("&"));
        List<String> data_2 = Arrays.asList(response.split(Constants.KEY_SPLITTER_1));
        if (data_1.get(0).equals("Checked")) {
            List<String> data = new ArrayList<>();
            for (int i = 1; i < data_1.size(); i++) {
                data.add(data_1.get(i));
            }

            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_recycler);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(v -> dialog.dismiss());
            dialog.setCancelable(false);
            CheckInOutAdapter2 adapter = new CheckInOutAdapter2(data);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.hasFixedSize();
            recyclerView.setAdapter(adapter);
            dialog.show();
        } else if (data_2.get(0).equals("Timesheet")) {
            List<String> data = new ArrayList<>();
            for (int i = 1; i < data_2.size(); i++) {
                data.add(data_2.get(i));
            }

            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_recycler);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(v -> dialog.dismiss());
            dialog.setCancelable(false);
            DisplayAdapter adapter = new DisplayAdapter(context, data);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.hasFixedSize();
            recyclerView.setAdapter(adapter);
            dialog.show();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMyDetailsInfo() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.KEY_DATABASE_LINK,
                this::handleMyDetails, error -> {
//            Supports support = new Supports(context);
//            support.alert_dialog(error.getMessage());
            progressBar.setVisibility(View.GONE);
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.KEY_REQUEST_TYPE, Constants.KEY_GET_MY_DETAILS_INFO);
                params.put(Constants.KEY_DATE, current_year + "-" + current_month);
                params.put(Constants.KEY_STAFF_ID, staffId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    // todo
    @SuppressLint("UseCompatLoadingForDrawables")
    private void handleMyDetails(String response) {
        progressBar.setVisibility(View.GONE);
        Supports support = new Supports(context);
         support.alert_dialog(response);
        List<String> list_1 = Arrays.asList(response.split(Constants.KEY_SPLITTER_1));
        for (int i=1; i <= getLastDate(); i++) {
            List<String> list_2 = Arrays.asList(list_1.get(i).split(Constants.KEY_SPLITTER_2));
            if (list_2.get(0).equals(Constants.KEY_0)) {
                if (!get_day(getTimestamp2(list_2.get(1))).equals("Sun")) {
                    setHighlighter2(getTimestamp2(list_2.get(1)), getResources().getDrawable(R.drawable.background_working), getResources().getColor(R.color.white));
                } else {
                    setHighlighter2(getTimestamp2(list_2.get(1)), getResources().getDrawable(R.drawable.background_holiday), getResources().getColor(R.color.white));
                }
            } else {
                switch (list_2.get(0)) {
                    case "W":
                    case "CO":
                    case "LH":
                        setHighlighter2(getTimestamp2(list_2.get(1)), getResources().getDrawable(R.drawable.background_present), getResources().getColor(R.color.nissi_blue));
                        break;
                    case "HO":
                        setHighlighter2(getTimestamp2(list_2.get(1)), getResources().getDrawable(R.drawable.background_holiday), getResources().getColor(R.color.white));
                        break;
                    case "LOP":
                    case "0LOP":
                    case "LOP0":
                        setHighlighter2(getTimestamp2(list_2.get(1)), getResources().getDrawable(R.drawable.background_working), getResources().getColor(R.color.white));
                        break;
                    case "HDSL":
                    case "HDSI":
                    case "HDCL":
                    case "HDEL":
                    case "HDPL":
                    case "HDML":
                        setHighlighter2(getTimestamp2(list_2.get(1)),getResources().getDrawable(R.drawable.background_halfworking_day_holiday), getResources().getColor(R.color.nissi_blue));
                        break;
                    case "SLHD":
                    case "SIHD":
                    case "CLHD":
                    case "ELHD":
                    case "PLHD":
                    case "MLHD":
                        setHighlighter2(getTimestamp2(list_2.get(1)),getResources().getDrawable(R.drawable.background_holiday_halfworking_day), getResources().getColor(R.color.nissi_blue));
                        break;
                    case "LOPHD":
                        setHighlighter2(getTimestamp2(list_2.get(1)),getResources().getDrawable(R.drawable.background_lop_halfworking_day), getResources().getColor(R.color.white));
                        break;
                    case "HDLOP":
                    case "HD":
                        setHighlighter2(getTimestamp2(list_2.get(1)),getResources().getDrawable(R.drawable.background_halfworking_day_lop), getResources().getColor(R.color.white));
                        break;
                    case "0SL":
                    case "0SI":
                    case "0CL":
                    case "0EL":
                    case "0PL":
                    case "0ML":
                        setHighlighter2(getTimestamp2(list_2.get(1)),getResources().getDrawable(R.drawable.background_low_leave), getResources().getColor(R.color.nissi_blue));
                        break;
                    case "SL0":
                    case "SI0":
                    case "CL0":
                    case "EL0":
                    case "PL0":
                    case "ML0":
                        setHighlighter2(getTimestamp2(list_2.get(1)),getResources().getDrawable(R.drawable.background_leave_low), getResources().getColor(R.color.nissi_blue));
                        break;
                    default:
                        setHighlighter2(getTimestamp2(list_2.get(1)), getResources().getDrawable(R.drawable.background_leave), getResources().getColor(R.color.nissi_blue));
                        break;
                }
            }
        }
    }


    private int getLastDate() {
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(current_year), Integer.parseInt(current_month), 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c.get(Calendar.DATE);
    }


    private void setHighlighter2(long timestamp, Drawable drawable, int color) {
        customViewList.add(new CustomView(timestamp, drawable, color));
        String day = getDay(timestamp);
        String month = getMonth(timestamp);
        String year = getYear(timestamp);

        if (current_month.equals(month) && current_year.equals(year)) {
            for (int i = 0; i < dates_list.size(); i++) {
                if (dates_list.get(i) != null && dates_list.get(i).equals(day)) {
                    text_date_list.get(i).setBackground(drawable);
                    text_date_list.get(i).setTextColor(color);
                    text_date_list.get(i).setTypeface(null, Typeface.BOLD);
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String get_day(long the_value) {
        SimpleDateFormat format = new SimpleDateFormat("E");
        return format.format(new Date(the_value));
    }

}

class CustomView {
    public long timestamp;
    public Drawable drawable;
    public int color;

    public CustomView(long timestamp, Drawable drawable, int color) {
        this.timestamp = timestamp;
        this.drawable = drawable;
        this.color = color;
    }
}
