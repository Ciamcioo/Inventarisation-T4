package com.mos.inventory.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
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
    private Date endDate;

    public ReservationRegister() { }

    public ReservationRegister(UUID reservationID, User userReservation, List<Equipment> equipmentReservationList, Date timeout, Date startDate, Date end_date) {
        this.reservationID = reservationID;
        this.userReservation = userReservation;
        this.equipmentReservationList = equipmentReservationList;
        this.timeout = timeout;
        this.startDate = startDate;
        this.endDate = end_date;
    }

    public ReservationRegister(User userReservation, List<Equipment> equipmentReservationList, Date timeout, Date startDate, Date endDate) {
        this.userReservation = userReservation;
        this.equipmentReservationList = equipmentReservationList;
        this.timeout = timeout;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationRegister that = (ReservationRegister) o;
        return Objects.equals(reservationID, that.reservationID) && Objects.equals(userReservation, that.userReservation) && Objects.equals(equipmentReservationList, that.equipmentReservationList) && Objects.equals(timeout, that.timeout) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationID, userReservation, equipmentReservationList, timeout, startDate, endDate);
    }
}
