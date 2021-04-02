package com.example.apsuevents;

public class User {
    public String name;
    public String email;
    public String phone;
    public String event;
    public String privacy;
    public String capacity;
    public String description;
    public String date;
    public String time;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String event, String privacy, String capacity, String description, String date, String time) {
        this.name = name;
        this.event=event;
        this.privacy=privacy;
        this.capacity=capacity;
        this.description=description;
        this.date=date;
        this.time=time;


    }
}
