package com.mos.inventory.dto;

import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.ReservationRegister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReservationContext {
    private ReservationRegister reservation;

    public ReservationContext() {

    }

    public ReservationContext(ReservationRegister reservation) {
        this.reservation = reservation;
    }

    public UUID getReservationUUID() {
       return reservation.getReservationID();
    }

    public Date getReservationStartDate() {
      return reservation.getStartDate();
    }

    public Date getReservationEndDate() {
        return reservation.getEndDate();
    }

    public List<EquipmentContext> getReservationEquipment() {
        List<EquipmentContext> equipmentContextList = new ArrayList<>();
        reservation.getEquipmentReservationList().forEach(equipment -> {
            equipmentContextList.add(new EquipmentContext(equipment));
        });
        return equipmentContextList;
    }
}
