package com.mos.inventory.service.mediator;

import com.mos.inventory.dto.CurrentSessionUser;
import com.mos.inventory.service.session.SessionService;
import com.mos.inventory.service.auth.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceMediator implements ServiceMediator {
    private LoginService loginService;
    private SessionService sessionService;

    @Autowired
    public UserServiceMediator(LoginService loginService, SessionService sessionService) {
        this.loginService = loginService;
        this.sessionService = sessionService;
    }


    @Override
    public void startCurrentUserSession(CurrentSessionUser sessionUser) {
        sessionService.setCurrentSessionUser(sessionUser);
    }
}
