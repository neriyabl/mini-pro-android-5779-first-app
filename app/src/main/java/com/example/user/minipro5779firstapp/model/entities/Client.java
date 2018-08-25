package com.example.user.minipro5779firstapp.model.entities;

/**
 * the object we wont to save in the database
 */
public class Client {
    private String name;
    private String phone;
    private String email;
    private ClientRequestStatus status;
    private double sourceLatitude;
    private double sourceLongitude;
    private String destination;


    // ------- constructors -------

    public Client(String name, String phone, String email,
                  double sourceLatitude, double sourceLongitude, String destination) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.status = ClientRequestStatus.wait;
        this.sourceLatitude = sourceLatitude;
        this.sourceLongitude = sourceLongitude;
        this.destination = destination;
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

    public ClientRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ClientRequestStatus status) {
        this.status = status;
    }

    public double getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(double sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public double getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(double sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
