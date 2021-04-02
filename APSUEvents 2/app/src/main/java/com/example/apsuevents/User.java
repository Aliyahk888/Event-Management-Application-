package com.example.apsuevents;

public class User {
    public String name;
    public String email;
    public String phone;
    public String event;
    public String privacy;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email,String phone, String event, String privacy) {
        this.name = name;
        this.email = email;
        this.event=event;
        this.phone=phone;
        this.privacy=privacy;

    }
}
