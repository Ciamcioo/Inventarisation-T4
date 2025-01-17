package com.mos.inventory.presistance;

import com.mos.inventory.entity.Equipment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EquipmentDAO {
    List<Equipment> findAllBy(Set<UUID> id);

    @Transactional
    void updateEquipment(Equipment equipment);

    List<Equipment> getEquipmentTable();
}
