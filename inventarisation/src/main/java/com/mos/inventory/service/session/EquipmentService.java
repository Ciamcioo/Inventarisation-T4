package com.mos.inventory.service.session;

import com.mos.inventory.dto.UpdateResult;

import java.util.UUID;

public interface EquipmentService {
    UpdateResult updateTechnicalStatusOf(UUID id, int technicalStatusID);
}
