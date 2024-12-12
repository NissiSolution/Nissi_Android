package com.nissisolution.nissibeta.Classes;

public class AdminMissedPunch {
    public String date, staff_name, the_data, period;
    public int status;
    public long timestamp;
    public boolean isChecked;

    public AdminMissedPunch(String date, String staff_name, String period, String the_data,
                            int status, long timestamp, boolean isChecked) {
        this.date = date;
        this.staff_name = staff_name;
        this.period = period;
        this.the_data = the_data;
        this.status = status;
        this.timestamp = timestamp;
        this.isChecked = isChecked;
    }
}
