package com.mos.inventory.service;

import com.mos.inventory.dto.UserContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class UserContextHolder {
    private final HttpSession session;

    public UserContextHolder(HttpSession session) {
        this.session = session;
    }


    public UserContext get() {
        return (UserContext) session.getAttribute("LOGGED_IN_USER");
    }

    public void set(UserContext userContext) {
        session.setAttribute("LOGGED_IN_USER", userContext);
    }

    public void remove(){
        session.removeAttribute("LOGGED_IN_USER");
    }
}
