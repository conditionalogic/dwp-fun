package com.bpdts.testapp.location;

import com.bpdts.testapp.client.model.retrofit.UserData;

public class Location
{
    private final double latitude;
    private final double longitude;


    public Location( double latitude, double longitude ) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location from( UserData userData ) {
        return new Location( userData.getLatitude(), userData.getLongitude() );
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    public String toString() {
        return latitude + "::" + longitude;
    }
}
