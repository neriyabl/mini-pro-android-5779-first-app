package com.example.user.minipro5779firstapp.model.entities;

import android.location.Location;

/**
 * the object we wont to save in the database
 */
public class Client {
    private String name;
    private String phone;
    private String email;
    private ClientRequestStatus status;
    private Location source;
    private Location destination;

    // ------- constructors -------


    public Client(String name, String phone, String email, Location source, Location destination) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.source = source;
        this.destination = destination;
        this.status = ClientRequestStatus.wait;
    }

    // --------- getters & setters --------


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
