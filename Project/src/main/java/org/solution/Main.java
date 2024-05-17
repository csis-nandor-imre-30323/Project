package org.solution;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        int choice1 = -1;
        int i = 1;
        int type;
        int hotelNumber = 0;
        int radius = 0;
        boolean f = false;
        String checkInDate;
        String checkOutDate;
        String name;
        String cleanliness;
        String review;
        Reservation res;
        ArrayList<Hotel> hotels = new ArrayList<>();
        User user;

        //Read in the data's from the Json file
        JSONArray hotelsArray = null;
        try {
            FileReader reader = new FileReader("hotels.json");
            JSONParser parser = new JSONParser();
            hotelsArray = (JSONArray) parser.parse(reader);
        } catch (Exception e) {
            System.out.println("Error reading JSON!");
        }

        for (Object o : hotelsArray) {
            JSONObject hotel = (JSONObject) o;
            hotels.add(new Hotel(((Long) hotel.get("id")).intValue(), hotel.get("name").toString(), (double) hotel.get("latitude"), (double) hotel.get("longitude"), LocalTime.parse(hotel.get("checkInTime").toString(), DateTimeFormatter.ISO_LOCAL_TIME), (JSONArray) hotel.get("rooms"), (JSONArray) hotel.get("feedbacks")));
        }

        //Local user creation for further action
        System.out.println("Welcome to my hotel management system.");
        System.out.println("I would like to ask your name for further action.");
        System.out.print("Your name: ");
        name = scanner.nextLine();
        System.out.println("Now I would like to ask you for your IP address in order to be able to locate you.");
        System.out.print("Your IP address: ");
        user = new User(name, scanner.nextLine());
        System.out.println("Now lets see what do you want to do: ");

        while (choice1 != 0) {
            System.out.println("1. Find hotels in your area\n2. Modify my reservation(delete or change)\n3. Leave a feedback\n0. Exit");
            System.out.print("Your input: ");
            choice1 = scanner.nextInt();
            switch (choice1) {
                case 1:
                    System.out.println("I would like to ask you to input a radius(km) in witch you would like to find a hotel.");
                    System.out.print("Your input is: ");
                    radius = scanner.nextInt();
                    System.out.println("Your input is: " + radius + "km.\nNow we check the availability of the hotels in this radius, just a moment!");
                    for (Hotel hotel : hotels) {
                        if (Math.round(user.distanceBetweenTwoPoints(hotel.getLatitudeMeters(), hotel.getLongitudeMeters())) / 1000 <= radius) {
                            System.out.print(i + ". " + hotel);
                            f = true;
                            i++;
                        }
                    }
                    if (!f) {
                        System.out.println("There were no hotels found in your area!");
                        break;
                    }
                    System.out.println("Choose one of the above listed hotels");
                    System.out.print("Your option: ");
                    hotelNumber = scanner.nextInt();
                    System.out.println("You chose: " + hotels.get(hotelNumber - 1));
                    System.out.println("It has the following rooms available: ");
                    hotels.get(hotelNumber - 1).printRooms();
                    choice = -1;
                    while (choice != 0) {
                        System.out.println("1. Reserve a room\n0. Exit");
                        System.out.print("Your choice: ");
                        choice = scanner.nextInt();
                        switch (choice) {
                            case 1:
                                while(true) {
                                    try {
                                        System.out.println("Enter in witch type of room do you want to stay at.");
                                        System.out.print("Your input: ");
                                        type = scanner.nextInt();
                                        System.out.println("Input your check in date as: YYYY-MM-DD");
                                        System.out.print("Your input: ");
                                        checkInDate = scanner.next();
                                        System.out.println("Input your check out date as: YYYY-MM-DD");
                                        System.out.print("Your input: ");
                                        checkOutDate = scanner.next();
                                        System.out.println(checkInDate);
                                        res = new Reservation(LocalDate.parse(checkInDate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.parse(checkOutDate, DateTimeFormatter.ISO_LOCAL_DATE));
                                        break;
                                    } catch (Exception e) {
                                        System.out.println("Incorrect date inputted!");
                                    }
                                }
                                f = hotels.get(hotelNumber - 1).addReservation(user.getUsername(), res, type);
                                if (!f) {
                                    System.out.println("No reservation was made!");
                                }
                                break;
                            case 0:
                                break;
                            default:
                                System.out.println("Please input a number between 0-1!");
                        }
                    }
                    break;
                case 2:
                    System.out.println("Enter in witch type of room did you make your reservation in.");
                    System.out.print("Your input: ");
                    type = scanner.nextInt();
                    choice = -1;
                    while (choice != 0) {
                        System.out.println("1. Delete a reservation\n2. Modify the reservation\n0. Exit");
                        System.out.print("Your option: ");
                        choice = scanner.nextInt();
                        switch (choice) {
                            case 1:
                                hotels.get(hotelNumber - 1).modifyReservation(user.getUsername(), type, choice, null);
                                break;
                            case 2:
                                while (true) {
                                    try {
                                        System.out.println("Input your new check in date as: YYYY-MM-DD");
                                        System.out.print("Your input: ");
                                        checkInDate = scanner.nextLine();
                                        System.out.println("Input your new check out date as: YYYY-MM-DD");
                                        System.out.print("Your input: ");
                                        checkOutDate = scanner.nextLine();
                                        res = new Reservation(LocalDate.parse(checkInDate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.parse(checkOutDate, DateTimeFormatter.ISO_LOCAL_DATE));
                                        hotels.get(hotelNumber - 1).modifyReservation(user.getUsername(), type, choice, res);
                                        break;
                                    } catch (DateTimeParseException e) {
                                        System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                                    }
                                }
                                break;
                            case 0:
                                break;
                            default:
                                System.out.println("Error, input a number between 0-1!");
                        }
                    }
                    break;
                case 3:
                    System.out.println("What hotel would you like to review?");
                    hotelNumber = 0;
                    for(Hotel hotel : hotels){
                        System.out.println(hotelNumber + ". " +hotel.getName());
                        hotelNumber++;
                    }
                    System.out.print("Your choice: ");
                    hotelNumber = scanner.nextInt();

                    System.out.println("How many start would you give out of 5?");
                    choice = -1;
                    while (choice > 5 || choice < 0) {
                        System.out.print("Your input: ");
                        choice = scanner.nextInt();
                    }
                    scanner.nextLine();
                    System.out.println("What would you say about the cleanliness of the room:");
                    cleanliness = scanner.nextLine();
                    System.out.println("Now write a small review about the hotel: ");
                    review = scanner.nextLine();
                    hotels.get(hotelNumber).addFeedback(new Feedback(user.getUsername(), choice, cleanliness, review));
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Incorrect input! Please input a number between 0-3!");
            }
        }

        //Update the Json file with the new data
        JSONArray hotelJson = new JSONArray();
        for (Hotel hotel : hotels) {
            hotelJson.add(hotel.toJson());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            FileWriter file = new FileWriter("hotels.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, hotelJson);

        } catch (Exception e) {
            System.out.println("Not working");
        }
    }
}