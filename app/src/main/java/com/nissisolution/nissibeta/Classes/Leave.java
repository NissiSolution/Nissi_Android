package com.nissisolution.nissibeta.Classes;

public class Leave {
    public String start_date, end_date, type;
    public int status, id;
    public double duration;
    public String the_data;
    public long timestamp;

    public Leave(String start_date, String end_date, String type, int status, double duration, String the_data, int id, long timestamp) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.type = type;
        this.status = status;
        this.duration = duration;
        this.the_data = the_data;
        this.id = id;
        this.timestamp = timestamp;
    }
}
