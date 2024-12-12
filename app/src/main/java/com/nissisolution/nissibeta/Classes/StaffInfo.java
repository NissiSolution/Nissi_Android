package com.nissisolution.nissibeta.Classes;

public class StaffInfo {
    public int staffid;
    public String firstname, lastname, staff_identifi;

    public StaffInfo() {
    }

    public StaffInfo(int staffid, String firstname, String lastname, String staff_identifi) {
        this.staffid = staffid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.staff_identifi = staff_identifi;
    }
}
