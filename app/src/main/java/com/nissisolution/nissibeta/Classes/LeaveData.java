package com.nissisolution.nissibeta.Classes;

public class LeaveData {
    public String from_date, to_date, pm, hr, number_of_days;
    public double pma, hra, status;
    public String purpose;

    public LeaveData(String from_date, String to_date, String pm, String hr, String number_of_days,
                     double pma, double hra, double status, String purpose) {
        this.from_date = from_date;
        this.to_date = to_date;
        this.pm = pm;
        this.hr = hr;
        this.number_of_days = number_of_days;
        this.pma = pma;
        this.hra = hra;
        this.status = status;
        this.purpose = purpose;
    }
}
