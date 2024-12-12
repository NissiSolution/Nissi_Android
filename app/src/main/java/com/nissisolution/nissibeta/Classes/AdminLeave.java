package com.nissisolution.nissibeta.Classes;

public class AdminLeave {
    public String staff_name, leave_type, start_date, end_date;
    public double duration;
    public int status, id;
    public String the_data;
    public long timestamp;
    public boolean isChecked;

    public AdminLeave(String staff_name, String leave_type, String start_date, String end_date,
                      double duration, int status, int id, String the_data, long timestamp, boolean isChecked) {
        this.staff_name = staff_name;
        this.leave_type = leave_type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.duration = duration;
        this.status = status;
        this.id = id;
        this.the_data = the_data;
        this.timestamp = timestamp;
        this.isChecked = isChecked;
    }
}
