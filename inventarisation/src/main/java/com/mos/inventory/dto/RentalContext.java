package com.mos.inventory.dto;

import com.mos.inventory.entity.RentalRegister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RentalContext {
    private RentalRegister rental;

    public RentalContext() { }

    public RentalContext(RentalRegister rental) {
        this.rental = rental;
    }

    public UUID getRentalUUID() {
        return rental.getId();
    }

    public Date getRentalStartDate() {
        return rental.getStartDate();
    }

    public Date getRentalEndDate() {
        return rental.getEndDate();
    }

    public List<EquipmentContext> getRentalEquipment() {
        List<EquipmentContext> equipmentContextList = new ArrayList<>();
        rental.getEquipmentRentalList().forEach(equipment ->
        {
            equipmentContextList.add(new EquipmentContext(equipment));
        });
        return equipmentContextList;
    }


}
