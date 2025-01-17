package com.mos.inventory.service.session;

import com.mos.inventory.dto.ReservationResult;
import com.mos.inventory.dto.UserContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public interface ReservationService {

    ReservationResult createReservation(Set<UUID> equipmentID, Date startDate, Date endDate);

    void setUserContextForService(UserContext userContext);
}
