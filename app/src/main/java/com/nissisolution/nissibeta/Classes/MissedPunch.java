package com.nissisolution.nissibeta.Classes;

public class MissedPunch {
    public String date, worked_hour, the_data;
    public int status, id;
    public long timestamp;

    public MissedPunch() {
    }

    public MissedPunch(String date, String worked_hour, int status, String the_data, int id, long timestamp) {
        this.date = date;
        this.worked_hour = worked_hour;
        this.status = status;
        this.the_data = the_data;
        this.id = id;
        this.timestamp = timestamp;
    }

}

