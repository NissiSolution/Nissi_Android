package com.nissisolution.nissibeta.Classes;

public class GuestHouseApplication {
    public int id, staffId;
    public String employeeName, employeeId;
    public long requestedOn, startDate, endDate;

    public GuestHouseApplication() {
    }

    public GuestHouseApplication(int id, int staffId, String employeeName, String employeeId,
                                 long requestedOn, long startDate, long endDate) {
        this.id = id;
        this.staffId = staffId;
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.requestedOn = requestedOn;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
