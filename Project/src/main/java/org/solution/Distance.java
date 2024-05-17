package org.solution;

public abstract class Distance {
    private double oneDegreeLatitude;
    private double oneDegreeLongitude;
    private long latitudeMeters;
    private long longitudeMeters;



    //calculates how many meters is a degree of latitude or longitude
    private void oneDegreeInLatitudeAndLongitude(double latitudeDegrees){
        oneDegreeLatitude = 111132.92 - 559.82 * Math.cos(2 * Math.toRadians(latitudeDegrees)) + 1.175 + Math.cos(4 * Math.toRadians(latitudeDegrees)) - 0.0023 * Math.cos(6 * Math.toRadians(latitudeDegrees));
        oneDegreeLongitude = 111412.84 * Math.cos(Math.toRadians(latitudeDegrees)) - 93.5 * Math.cos(3 * Math.toRadians(latitudeDegrees)) + 0.118 * Math.cos(5 * Math.toRadians(latitudeDegrees));
    }

    public void convertLatitudeAndLongitudeToMeters(double latitude, double longitude){
        oneDegreeInLatitudeAndLongitude(latitude);
        latitudeMeters = Math.round(latitude * oneDegreeLatitude);
        longitudeMeters = Math.round(longitude * oneDegreeLongitude);
    }

    public long distanceBetweenTwoPoints(long latitude, long longitude){
        return Math.round(Math.sqrt(Math.pow(latitudeMeters - latitude, 2) + Math.pow(longitudeMeters - longitude, 2)));
    }

    public long getLatitudeMeters() {
        return latitudeMeters;
    }

    public long getLongitudeMeters() {
        return longitudeMeters;
    }
}
