package com.nissisolution.nissibeta.Classes;

public class CheckedInfo {
    public String staff_name;
    public double longitude, latitude;
    public String date;
    public int type;
    public String data;

    public CheckedInfo(String staff_name, double longitude, double latitude, String date, int type, String data) {
        this.staff_name = staff_name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.type = type;
        this.data = data;
    }
}
