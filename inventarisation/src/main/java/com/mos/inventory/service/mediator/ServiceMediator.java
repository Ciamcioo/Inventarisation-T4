package com.mos.inventory.service.mediator;

import com.mos.inventory.dto.CurrentSessionUser;

public interface ServiceMediator {
    void startCurrentUserSession(CurrentSessionUser sessionUser);
}
