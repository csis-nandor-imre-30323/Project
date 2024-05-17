package org.solution;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

public class Locate {

    private Double latitude;
    private Double longitude;
    private final IPinfo ipInfo = new IPinfo.Builder().setToken("9b33141db34d69").build();;

    public Locate(String ip) {
        try {
            IPResponse response = ipInfo.lookupIP(ip);
            latitude = Double.valueOf(response.getLatitude());
            longitude = Double.valueOf(response.getLongitude());
        } catch (RateLimitedException | NullPointerException e) {
            System.out.println("Error! The Ip address is not correct!");
        }
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}