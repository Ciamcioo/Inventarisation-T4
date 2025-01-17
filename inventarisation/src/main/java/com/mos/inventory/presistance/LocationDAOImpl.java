package com.mos.inventory.presistance;

import com.mos.inventory.entity.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationDAOImpl implements LocationDAO {
    private final EntityManager entityManager;

    public LocationDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Location> getLocationTable() {
        TypedQuery<Location> query = entityManager.createQuery("FROM Location", Location.class);
        return query.getResultList();
    }
}
