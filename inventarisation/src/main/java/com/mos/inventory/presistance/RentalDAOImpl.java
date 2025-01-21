package com.mos.inventory.presistance;

import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.RentalRegister;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Transactional
public class RentalDAOImpl implements RentalDAO {
    private final EntityManager entityManager;


    public RentalDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<RentalRegister> findRentalsFor(UUID userID) {
        TypedQuery<RentalRegister> query = entityManager.createQuery("FROM RentalRegister" +
                                                                        " WHERE userRental.id=:userID",
                                                                        RentalRegister.class);
        query.setParameter("userID", userID);
        return query.getResultList();
    }

    @Override
    public boolean checkIfContainsRental(UUID rentalID) {
        RentalRegister rental = entityManager.find(RentalRegister.class, rentalID);
        return checkIfRentalIsNotNull(rental);

    }

    @Override
    public void removeRental(UUID rentalID) {
        RentalRegister rental = entityManager.find(RentalRegister.class, rentalID);
        entityManager.remove(rental);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public void insertRental(RentalRegister newRental) {
        try {
            List<Equipment> rentalEquipment = newRental.getEquipmentRentalList().stream()
                    .map(equipment -> entityManager.contains(equipment) ? equipment : entityManager.merge(equipment)).collect(Collectors.toList());;

            newRental.setEquipmentRentalList(rentalEquipment);
            entityManager.persist(newRental);
        } catch(HibernateException exception) {
            throw new InvalidInsert("Rental couldn't be inserted into table");

        }

    }
    private boolean checkIfRentalIsNotNull(RentalRegister rental) {
        return rental != null;
    }



}
