package com.example.davidalaw.bsllearningtool.mData;

/**
 * Setters and getters for Region object
 *
 */

public class Regions {

    private int region_id;
    private String region_name;
    private float longitude, latitude;

    public Regions() {
    }

    public Regions(String region_name, float longitude, float latitude) {
        this.region_name = region_name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * String split method - tokenise the string.
     *
     * @param line
     */
    public Regions(String line) {
        String[] tokens = line.split(";");
        region_name = tokens[0];
        longitude = Float.valueOf(tokens[1]);
        latitude = Float.valueOf(tokens[2]);
    }

    public int getRegion_id() {
        return region_id;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}

