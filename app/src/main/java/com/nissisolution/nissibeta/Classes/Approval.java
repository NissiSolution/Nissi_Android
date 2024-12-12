package com.nissisolution.nissibeta.Classes;


public class Approval {

    public int staff_id, status;
    public long timestamp;

    public Approval(int staff_id, int status, long timestamp) {
        this.staff_id = staff_id;
        this.status = status;
        this.timestamp = timestamp;
    }
}
