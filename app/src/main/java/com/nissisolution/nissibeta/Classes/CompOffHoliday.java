package com.nissisolution.nissibeta.Classes;

public class CompOffHoliday {
    public String date, request_type, the_data;
    public int status, id;
    public long timestamp;

    public CompOffHoliday() {
    }

    public CompOffHoliday(String date, String request_type, int status, String the_data, int id, long timestamp) {
        this.date = date;
        this.request_type = request_type;
        this.status = status;
        this.the_data = the_data;
        this.id = id;
        this.timestamp = timestamp;
    }
}
