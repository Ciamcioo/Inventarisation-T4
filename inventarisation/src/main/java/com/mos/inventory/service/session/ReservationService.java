package com.mos.inventory.service.session;

import com.mos.inventory.dto.EquipTransactionResult;
import com.mos.inventory.dto.UserContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public interface ReservationService {

    EquipTransactionResult createReservation(Set<UUID> equipmentID, Date startDate, Date endDate, String transactionType);

    void setUserContextForService(UserContext userContext);
}
