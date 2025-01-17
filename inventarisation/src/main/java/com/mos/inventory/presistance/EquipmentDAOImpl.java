package com.mos.inventory.presistance;

import com.mos.inventory.entity.Equipment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@Transactional
public class EquipmentDAOImpl implements  EquipmentDAO {
    private final EntityManager entityManager;

    public EquipmentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Equipment> findAllBy(Set<UUID> ids) {
        TypedQuery<Equipment> query;
        query = entityManager.createQuery("From Equipment " +
                                             "WHERE id " +
                                             "IN :equipmentIDs", Equipment.class);
        query.setParameter("equipmentIDs", ids);
        return query.getResultList();
    }

    @Override
    public void updateEquipment(Equipment equipment) {
        try {
            entityManager.merge(equipment);
        } catch (HibernateException exception) {
            throw new UpdatedFailException("Equipment update failed");
        }
    }

    @Override
    public List<Equipment> getEquipmentTable() {
        TypedQuery<Equipment> query = entityManager.createQuery("FROM Equipment", Equipment.class);
        return query.getResultList();
    }

    public static class UpdatedFailException extends RuntimeException {
        public UpdatedFailException(String message) {
            super(message);
        }

    }
}
