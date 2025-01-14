package com.mos.inventory.service.session;

import com.mos.inventory.dto.CurrentSessionUser;

public interface SessionService {

    void setCurrentSessionUser(CurrentSessionUser currentSessionUser);
}
