package com.mos.inventory.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservation_register")
public class ReservationRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reservation_id")
    private UUID reservationID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userReservation;

    @ManyToMany
    @JoinTable(
            name = "reservation_equipment",
            joinColumns = { @JoinColumn(name = "reservation_id")},
            inverseJoinColumns = { @JoinColumn(name = "equipment_id")}
    )
    private List<Equipment> equipmentReservationList;

    @Column(name = "timeout")
    @Temporal(TemporalType.DATE)
    private Date timeout;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date end_date;

    public ReservationRegister() { }

    public ReservationRegister(UUID reservationID, User userReservation, List<Equipment> equipmentReservationList, Date timeout, Date startDate, Date end_date) {
        this.reservationID = reservationID;
        this.userReservation = userReservation;
        this.equipmentReservationList = equipmentReservationList;
        this.timeout = timeout;
        this.startDate = startDate;
        this.end_date = end_date;
    }

    public UUID getReservationID() {
        return reservationID;
    }

    public void setReservationID(UUID reservationID) {
        this.reservationID = reservationID;
    }

    public User getUserReservation() {
        return userReservation;
    }

    public void setUserReservation(User userReservation) {
        this.userReservation = userReservation;
    }

    public List<Equipment> getEquipmentReservationList() {
        return equipmentReservationList;
    }

    public void setEquipmentReservationList(List<Equipment> equipmentReservationList) {
        this.equipmentReservationList = equipmentReservationList;
    }

    public Date getTimeout() {
        return timeout;
    }

    public void setTimeout(Date timeout) {
        this.timeout = timeout;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
}
