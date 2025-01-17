package com.mos.inventory.dto;

import java.util.Objects;

public class LoginResult {
    private LoginMessage message;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userRoleName;

    public LoginResult(LoginMessage loginMessage, String email) {
        this.message = loginMessage;
        this.userEmail = email;
    }

    public LoginResult(LoginMessage loginMessage, String firstName, String lastName, String email, String roleName) {
        this.message = loginMessage;
        this.userEmail = email;
        this.userFirstName = firstName;
        this.userLastName = lastName;
        this.userRoleName = roleName;
    }

    public LoginMessage getMessage() {
        return message;
    }

    public void setMessage(LoginMessage message) {
        this.message = message;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResult that = (LoginResult) o;
        return message == that.message && Objects.equals(userFirstName, that.userFirstName) && Objects.equals(userLastName, that.userLastName) && Objects.equals(userEmail, that.userEmail) && Objects.equals(userRoleName, that.userRoleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, userFirstName, userLastName, userEmail, userRoleName);
    }
}
