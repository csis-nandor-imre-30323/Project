package org.solution;

import org.json.simple.JSONObject;

import java.time.LocalDate;

public class Feedback {
    private final String name;
    private final int stars; //number between 0-5
    private LocalDate date;
    private final String cleanliness; //small text about the cleanliness
    private final String review; //other comment if it is the case

    public Feedback(String name, int stars, String cleanliness, String review) {
        this.name = name;
        this.date = LocalDate.now();
        this.stars = stars;
        this.cleanliness = cleanliness;
        this.review = review;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("stars", stars);
        json.put("date", date.toString());
        json.put("cleanliness", cleanliness);
        json.put("review", review);

        return json;
    }
}
