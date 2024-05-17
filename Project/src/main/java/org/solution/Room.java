package org.solution;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Room {
    private final int roomNumber;
    private final int type;
    private final BigDecimal price;
    //String: name of the person who reserved the room, Reservation: check-in date and check-out date
    private final HashMap<String, Reservation> reservations = new HashMap<>();

    //todo: remove this after fill up


    public Room(int roomNumber, int type, BigDecimal price) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
    }

    public Room(int roomNumber, int type, BigDecimal price, JSONArray reservationsJson) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        addReservationsFromJson(reservationsJson);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("roomNumber", this.roomNumber);
        json.put("type", this.type);
        json.put("price", this.price);

        JSONArray reservationsJson = new JSONArray();

        for (String key : reservations.keySet()) {
            JSONObject reservationJson = new JSONObject();
            reservationJson.put("reservationName", key); //key: the name of the person who reserved the room
            reservationJson.put("checkInDate", reservations.get(key).getCheckInDate().toString());
            reservationJson.put("checkOutDate", reservations.get(key).getCheckOutDate().toString());
            reservationsJson.add(reservationJson);
        }
        json.put("reservations", reservationsJson);

        return json;
    }

    public void addReservationsFromJson(JSONArray res){
        for(Object o : res){
            JSONObject reservation = (JSONObject) o;
            reservations.put(reservation.get("reservationName").toString(), new Reservation(LocalDate.parse(reservation.get("checkInDate").toString(), DateTimeFormatter.ISO_LOCAL_DATE),LocalDate.parse(reservation.get("checkOutDate").toString(), DateTimeFormatter.ISO_LOCAL_DATE)));
        }
    }

    public boolean addReservation(String name, Reservation res){
        if(!checkReservation(res)){
            return false;
        }
        System.out.println("Your reservation on the following dates: " + res.getCheckInDate() + " - " + res.getCheckOutDate() + " it was accepted, you will be staying in room with number: "+ this.roomNumber + ", we are waiting for you!");
        reservations.put(name, res);
        return true;
    }

    private boolean checkReservation(Reservation res){
        for(Reservation reservation : reservations.values() ) {
            if (!(res.getCheckOutDate().compareTo(reservation.getCheckInDate()) <= 0 || //if the check-out is before or on the same day as the other check in
                    res.getCheckInDate().compareTo(reservation.getCheckOutDate()) >= 0)){ //if the check-in is after or on the same day as the other check out
                return false;
            }
        }
        return true;
    }

    public void deleteReservation(String name){
        reservations.remove(name);
    }

    public int getType() {
        return type;
    }

    public Reservation findReservation(String name){
        return reservations.get(name);
    }

    public boolean changeDates(String name, Reservation res) {
        Reservation save = reservations.get(name);
        reservations.remove(name);
        if(addReservation(name, res)){
            return true;
        }
        else{
            reservations.put(name, save);
            return false;
        }
    }

    @Override
    public String toString() {
        return "Room number: " + roomNumber +
                "\ntype: " + type +
                "\nprice:" + price + "\n";
    }
}
