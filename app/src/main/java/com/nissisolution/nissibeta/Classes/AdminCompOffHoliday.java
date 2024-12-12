package com.nissisolution.nissibeta.Classes;

public class AdminCompOffHoliday {
    public String date, staff_name, request_type, the_data;
    public int status;
    public long timestamp;
    public boolean isChecked;

    public AdminCompOffHoliday(String date, String staff_name, String request_type, String the_data, int status, long timestamp, boolean isChecked) {
        this.date = date;
        this.staff_name = staff_name;
        this.request_type = request_type;
        this.the_data = the_data;
        this.status = status;
        this.timestamp = timestamp;
        this.isChecked = isChecked;
    }
}
