package com.mos.inventory.presistance;

import com.mos.inventory.entity.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatusDAOImpl implements StatusDAO{
    private final EntityManager entityManager;

    public StatusDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Status> getStatusTable() {
        TypedQuery<Status> query = entityManager.createQuery("From Status", Status.class);
        return query.getResultList();
    }
}
