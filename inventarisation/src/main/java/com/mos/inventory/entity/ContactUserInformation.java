package com.mos.inventory.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "contact_user_information")
public class ContactUserInformation {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userID;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public ContactUserInformation() { }

    public ContactUserInformation(UUID userID, String phoneNumber, String email, User user) {
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.user = user;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
