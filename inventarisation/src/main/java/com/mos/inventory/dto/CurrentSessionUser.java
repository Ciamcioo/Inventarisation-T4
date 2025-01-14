package com.mos.inventory.dto;

import com.mos.inventory.entity.User;

public class CurrentSessionUser {
    private User  entityUser;

    public CurrentSessionUser() { }

    public CurrentSessionUser(User entityUser) {
        this.entityUser = entityUser;
    }

    public User getEntityUser() {
        return entityUser;
    }

    public void setEntityUser(User entityUser) {
        this.entityUser = entityUser;
    }
}
