package com.mos.inventory.presistance;

import com.mos.inventory.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserDAO {
    List<User> findAll();

    User findBy(UUID id);
}
