package com.mos.inventory.service.session;

import com.mos.inventory.dto.EquipTransactionResult;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public interface RentalService {

    EquipTransactionResult createRental(Set<UUID> equipmentIDs, Date startDate, Date endDate, String transactionType);

    EquipTransactionResult returnRental(UUID rentalID);

}
