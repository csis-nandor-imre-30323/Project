package org.solution;

public class User extends Distance {

    private String userName;
    private Locate location;

    public User() {
    }

    public User(String username, String ip) {
        this.userName = username;
        location = new Locate(ip);
        super.convertLatitudeAndLongitudeToMeters(location.getLatitude(), location.getLongitude());

    }
    public String getUsername() {
        return userName;
    }

    public Locate getLocation() {
        return location;
    }
}
