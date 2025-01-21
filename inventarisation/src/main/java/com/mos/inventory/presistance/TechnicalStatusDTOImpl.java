package com.mos.inventory.presistance;


import com.mos.inventory.entity.TechnicalStatus;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TechnicalStatusDTOImpl implements TechnicalStatusDTO{
    private final EntityManager entityManager;

    public TechnicalStatusDTOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<TechnicalStatus> getTechnicalStatusBy(int id) {
       return Optional.ofNullable(entityManager.find(TechnicalStatus.class, id));
    }
}
