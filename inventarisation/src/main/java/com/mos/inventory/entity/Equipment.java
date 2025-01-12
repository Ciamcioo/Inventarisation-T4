package com.mos.inventory.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "technical_status_id")
    private TechnicalStatus technicalStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "equipmentReservationList")
    List<ReservationRegister> reservations;

    @ManyToMany(mappedBy = "equipmentRentalList")
    List<RentalRegister> rentals;

    public Equipment() { }

    public Equipment(UUID id, String name, int quantity, String description, Location location, Status status, TechnicalStatus technicalStatus, Category category, List<RentalRegister> rentals, List<ReservationRegister> reservations) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.description = description;
        this.location = location;
        this.status = status;
        this.technicalStatus = technicalStatus;
        this.category = category;
        this.rentals = rentals;
        this.reservations = reservations;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TechnicalStatus getTechnicalStatus() {
        return technicalStatus;
    }

    public void setTechnicalStatus(TechnicalStatus technicalStatus) {
        this.technicalStatus = technicalStatus;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setReservations(List<ReservationRegister> reservations) {
        this.reservations = reservations;
    }

    public void setRentals(List<RentalRegister> rentals) {
        this.rentals = rentals;
    }
}
