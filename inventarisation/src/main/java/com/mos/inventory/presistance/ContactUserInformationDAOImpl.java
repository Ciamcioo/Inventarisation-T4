package com.mos.inventory.presistance;

import com.mos.inventory.entity.ContactUserInformation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class ContactUserInformationDAOImpl implements ContactUserInformationDAO {
    private final EntityManager entityManager;

    public ContactUserInformationDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    @Override
    public ContactUserInformation findBy(String email) {
        TypedQuery<ContactUserInformation> query = entityManager.createQuery("FROM ContactUserInformation WHERE email=:userEmail", ContactUserInformation.class);
        query.setParameter("userEmail", email);
        return getSingleObjectFrom(query);
    }

    public ContactUserInformation getSingleObjectFrom(TypedQuery<ContactUserInformation> query) {
        try {
            return query.getSingleResult();
        } catch(NoResultException exception) {
            return null;
        }
    }
}
