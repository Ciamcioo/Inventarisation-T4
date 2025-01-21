package com.mos.inventory.presistance;

import com.mos.inventory.entity.TechnicalStatus;

import java.util.Optional;

public interface TechnicalStatusDTO {

    Optional<TechnicalStatus> getTechnicalStatusBy(int id);
}
