package com.example.apsuevents;

public class Deets {
    public String fullname;
    public String email;
    public String phone;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Deets() {
    }

    public Deets(String fullname, String email, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
    }

}
