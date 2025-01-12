package com.mos.inventory.presistance;

import com.mos.inventory.entity.ContactUserInformation;
import com.mos.inventory.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserDAOImp implements UserDAO{
    private EntityManager entityManager;

    @Autowired
    public UserDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("FROM User", User.class);
        List<User> users = query.getResultList();
        return users;
    }

    @Override
    public User findBy(UUID userID) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE id=:userID" , User.class);
        query.setParameter("userID", userID);
        return getSingleObjectFrom(query);
    }

    public User getSingleObjectFrom(TypedQuery<User> query) {
        try {
            return query.getSingleResult();
        } catch(NoResultException exception) {
            return null;
        }
    }

}
