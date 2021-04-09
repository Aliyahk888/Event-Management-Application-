package com.example.apsuevents;

public class User {
    public String name;
    public String event;
    public String privacy;
    public String capacity;
    public String description;
    public String date;
    public String time;
    public String password;
    public String cur_cap;
    public String place;
    public String attendee;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String event, String privacy, String capacity, String cur_capacity, String description, String date, String time, String password, String Place, String attendee) {
        this.name = name;
        this.event = event;
        this.privacy = privacy;
        this.capacity = capacity;
        this.description = description;
        this.date = date;
        this.time = time;
        this.password = password;
        this.cur_cap = cur_capacity;
        this.place = Place;
        this.attendee=attendee;

    }


    public String getName() {
        return name;
    }

    public String getEvent() {
        return event;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getDate() {
        return date;
    }


    public String getCurCapacity() { return cur_cap; }

}




