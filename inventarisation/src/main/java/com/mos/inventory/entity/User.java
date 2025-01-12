package com.mos.inventory.entity;


import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ContactUserInformation contactInformation;

    @OneToMany(mappedBy = "userRental")
    private List<RentalRegister> rentals;

    @OneToMany(mappedBy = "userReservation")
    private List<ReservationRegister> reservations;



    public User() { }

    public User(String firstName, String lastName, Role role, Date joiningDate, ContactUserInformation contactInformation, List<RentalRegister> rentals, List<ReservationRegister> reservations) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.joiningDate = joiningDate;
        this.contactInformation = contactInformation;
        this.rentals = rentals;
        this.reservations = reservations;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public ContactUserInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactUserInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public List<RentalRegister> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalRegister> rentals) {
        this.rentals = rentals;
    }

    public List<ReservationRegister> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationRegister> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fistName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role.getName()+
                ", joiningDate=" + joiningDate +
                '}';
    }
}
