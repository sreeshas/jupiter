package com.jupiter.application.domain;

/**
 * Created by sreenidhisreesha on 10/25/14.
 */
public class Location {

    public String type = "Point";

    public float[] coordinates;

    public Location (float latitude, float longitude) {
        this.coordinates = new float[]{longitude, latitude};

    }

    public Location() {

    }
}
