package com.nissisolution.nissibeta.Classes;

public class SingleGuestHouse {
    public int position;
    public String name, map_url, image_url;
    public int id;

    public SingleGuestHouse() {
    }

    public SingleGuestHouse(int position, String name, String map_url, String image_url, int id) {
        this.position = position;
        this.name = name;
        this.map_url = map_url;
        this.image_url = image_url;
        this.id = id;
    }
}
