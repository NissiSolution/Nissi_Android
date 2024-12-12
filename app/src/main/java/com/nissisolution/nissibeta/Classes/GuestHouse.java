package com.nissisolution.nissibeta.Classes;

public class GuestHouse {

    public SingleGuestHouse left_guest_house, right_guest_house;
    public int count;

    public GuestHouse() {
    }

    public GuestHouse(SingleGuestHouse left_guest_house, SingleGuestHouse right_guest_house, int count) {
        this.left_guest_house = left_guest_house;
        this.right_guest_house = right_guest_house;
        this.count = count;
    }
}
