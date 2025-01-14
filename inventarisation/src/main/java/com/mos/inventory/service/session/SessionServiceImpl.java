package com.mos.inventory.service.session;

import com.mos.inventory.dto.CurrentSessionUser;
import com.mos.inventory.service.mediator.ServiceMediator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    private ServiceMediator mediator;
    private CurrentSessionUser currentSessionUser;

    @Autowired
    public SessionServiceImpl(ServiceMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void setCurrentSessionUser(CurrentSessionUser currentSessionUser) {
        this.currentSessionUser = currentSessionUser;
    }
}
