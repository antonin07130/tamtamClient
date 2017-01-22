package com.tamtam.android.tamtam.model;

/**
 * Created by antoninpa on 22/01/17.
 */


/**
 * This class represents a position (longitude, latitude)
 */
public class PositionObject implements java.io.Serializable{
    private final double longitude;
    private final double latitude;

    /**
     * to prepare GeoJson std, the order of positions should be lon, lat, [altitude]
     * @param longitude longitude value
     * @param latitude latitude value
     */
    public PositionObject(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public double getLongitude() { return longitude; }
    public double getLatitude(){
        return latitude;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionObject that = (PositionObject) o;

        if (Double.compare(that.longitude, longitude) != 0) return false;
        return Double.compare(that.latitude, latitude) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(longitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "PositionObject{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}