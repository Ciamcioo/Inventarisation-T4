package com.mos.inventory.dto;

import com.mos.inventory.entity.Role;
import com.mos.inventory.entity.User;
import jakarta.servlet.http.HttpSession;

import java.util.Objects;
import java.util.UUID;

public class UserContext {

    private User user;
    private String email;

    public UserContext() {
    }

    public UserContext(User user, String email){
        this.user = user;
        this.email = email;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getUserID() {
        return user.getId();
    }

    public String getUserRoleName() {
        return user.getRole().getName();
    }

    // Method for testing
    public void setUserRoleDescription(String roleDescription) {
        if (this.user.getRole() == null) {
           this.user.setRole(new Role());
        }

        this.user.getRole().setName(roleDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserContext that = (UserContext) o;
        return Objects.equals(user, that.user) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, email);
    }
}
