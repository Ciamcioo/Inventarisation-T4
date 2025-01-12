package com.mos.inventory.presistance;

import com.mos.inventory.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDAOImp implements RoleDAO {

    private EntityManager entityManager;

    @Autowired
    public RoleDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Role> findAll() {
        TypedQuery<Role> query = entityManager.createQuery("FROM Role", Role.class);
        List<Role> roles = query.getResultList();
        return roles;
    }
}
