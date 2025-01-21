package com.mos.inventory.dto;

import com.mos.inventory.entity.Equipment;

public class EquipmentContext {
    private Equipment equipment;

    public EquipmentContext(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getEquipmentName() {
       return equipment.getName();
    }

    public String getEquipmentDescription() {
        return equipment.getDescription();

    }

}
