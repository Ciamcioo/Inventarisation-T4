package com.mos.inventory.presistance;

import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.ReservationRegister;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Transactional
public class ReservationDAOImpl implements ReservationDAO {
    private final EntityManager entityManager;

    public ReservationDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public List<ReservationRegister> findReservationsBy(UUID userID) {
        TypedQuery<ReservationRegister> query;
        query = entityManager.createQuery("FROM ReservationRegister " +
                                            "WHERE userReservation.id=:userID",
                                            ReservationRegister.class);
        query.setParameter("userID", userID);
        return query.getResultList();
    }

    @Override
    public void insertReservation(ReservationRegister newReservations) {
        try {
            List<Equipment> managedEquipment = newReservations.getEquipmentReservationList().stream()
                            .map(equipment -> entityManager.contains(equipment) ? equipment : entityManager.merge(equipment)).collect(Collectors.toList());

            newReservations.setEquipmentReservationList(managedEquipment);
            entityManager.persist(newReservations);
            entityManager.flush();
        } catch(HibernateException exception) {
            throw new InvalidInsert("Reservation couldn't be inserted into table");
        }
    }

}
