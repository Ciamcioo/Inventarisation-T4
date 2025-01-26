package com.mos.inventory.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rental_register")
public class RentalRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userRental;

    @ManyToMany
    @JoinTable(
            name = "rental_equipment",
            joinColumns = {@JoinColumn(name = "rental_id")},
            inverseJoinColumns = {@JoinColumn(name = "equipment_id")}
    )
    private List<Equipment> equipmentRentalList;


    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;


    public RentalRegister() { }

    public RentalRegister(User userRental, List<Equipment> equipmentRental, Date startDate, Date endDate) {
        this.userRental = userRental;
        this.equipmentRentalList = equipmentRental;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public RentalRegister(UUID id, User userRental, List<Equipment> equipmentRentalList, Date startDate, Date endDate) {
        this.id = id;
        this.userRental = userRental;
        this.equipmentRentalList = equipmentRentalList;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUserRental() {
        return userRental;
    }

    public void setUserRental(User userRental) {
        this.userRental = userRental;
    }

    public List<Equipment> getEquipmentRentalList() {
        return equipmentRentalList;
    }

    public void setEquipmentRentalList(List<Equipment> equipmentRentalList) {
        this.equipmentRentalList = equipmentRentalList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
