package org.solution;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hotel extends Distance {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Room> rooms = new ArrayList<>();
    private LocalTime checkInTime;
    private List<Feedback> feedbacks = new ArrayList<>(); //String: name of the person who wrote a feedback


    //todo: take this constructor out after filling the json file


    public Hotel(int id, String name, double latitude, double longitude, List<Room> rooms, LocalTime checkInTime, List<Feedback> feedbacks) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rooms = rooms;
        this.checkInTime = checkInTime;
        this.feedbacks = feedbacks;
    }

    //todo: till this take out

    public Hotel(int id, String name, double latitude, double longitude, LocalTime checkInTime, JSONArray roomsJson, JSONArray feedbackJson) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.checkInTime = checkInTime;
        addRoomsFromJson(roomsJson);
        addFeedbackFromJson(feedbackJson);
        super.convertLatitudeAndLongitudeToMeters(latitude, longitude);
    }

    public JSONObject toJson() {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObj.put("id", this.id);
        jsonObj.put("name", this.name);
        jsonObj.put("latitude", this.latitude);
        jsonObj.put("longitude", this.longitude);
        jsonObj.put("checkInTime", this.checkInTime.toString());
        
        for (Room room : rooms) {
            jsonArray.add(room.toJson());
        }
        jsonObj.put("rooms", jsonArray);

        jsonArray = new JSONArray();
        for (Feedback feedback : feedbacks) {
            jsonArray.add(feedback.toJson());
        }
        jsonObj.put("feedbacks", jsonArray);

        return jsonObj;
    }

    private void addRoomsFromJson(JSONArray roomsJson){
        int i = 0;
        for (Object o : roomsJson){
            JSONObject room = (JSONObject) o;
            rooms.add(new Room(((Long)room.get("roomNumber")).intValue(), ((Long)room.get("type")).intValue(), BigDecimal.valueOf((Long) room.get("price")),(JSONArray) room.get("reservations")));
        }
    }

    private void addFeedbackFromJson(JSONArray feedbacksJson){
        for(Object o : feedbacksJson){
            JSONObject feedback = (JSONObject) o;
            feedbacks.add(new Feedback(feedback.get("name").toString(), ((Long)feedback.get("stars")).intValue(), feedback.get("cleanliness").toString(), feedback.get("review").toString()));
        }
    }

    public boolean addReservation(String name, Reservation res, int type){
        if(!res.getCheckInDate().isBefore(res.getCheckOutDate())){
            System.out.println("Incorrect reservation dates! Please try again!");
            return false;
        }
        boolean f = false;

        for(Room room : rooms){
            if(room.getType() == type){
                f = true;
                if(room.addReservation(name, res)){
                    return true;
                }
            }
        }

        if(!f){
            System.out.println("There is no room of this type!");
        }
        else{
            System.out.println("All of the rooms of this type: " + type + " are occupied on your required dates.");
            System.out.println("Please try again on different dates.");
        }
        return false;
    }

    public boolean modifyReservation(String name, int type, int choice, Reservation res){
        Room room = findReservedRoom(name);
        boolean f = false;
        if(room != null){
            if(!validateChangeReservation(name, room)) {   //if it is at leas two hours before the check-in time
                switch(choice){
                    case 0:
                        room.deleteReservation(name);
                        System.out.println("The reservation of room with of this type: " + type + " created by: " + name + " was removed.");
                        f = true;
                        break;
                    case 1:
                        f = room.changeDates(name, res);
                        if(!f){
                            System.out.println("Your reservation could not be changed, please try again!");
                        }
                        break;
                }
                return f;
            }
            else{
                System.out.println("You can modify your reservation only 2 hours before: " + checkInTime);
            }
        }
        else {
            System.out.println("There was no reservation found by : " + name);
        }
        return f;
    }
    
    public void addFeedback(Feedback feedback){
        this.feedbacks.add(feedback);
    }

    public void printRooms(){
        for(Room room : rooms){
            System.out.println(room);
        }
    }

    public String getName() {
        return name;
    }

    //returns true if the reservation can be modified or not
    private boolean validateChangeReservation(String name, Room room){
        //if it is at leas two hours before the check-in time
        return LocalTime.now().isBefore(checkInTime.minusHours(2)) && LocalDate.now().isBefore(room.findReservation(name).getCheckInDate());
    }

    private Room findReservedRoom(String name){
        for (Room room : rooms) {
            if (room.findReservation(name) != null) {
                return room;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Hotel name: " + name + "\n";
    }
}