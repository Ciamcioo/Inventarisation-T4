package com.mos.inventory.service.session;

import com.mos.inventory.dto.EquipTransactionResult;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EquipTxnValidator {

    Optional<EquipTransactionResult> validateInputData(Set<UUID> equipmentIDs, Date startDate, Date endDate, String transactionType);
    Optional<EquipTransactionResult> checkIfUserContextIsPresent();
    Optional<EquipTransactionResult> equipmentValidation(Set<UUID> equipmentIDs);
    Optional<EquipTransactionResult> dateValidation(Date startDate, Date endDate);
    Optional<EquipTransactionResult> transactionPolicyValidation(String transactionType);
}
