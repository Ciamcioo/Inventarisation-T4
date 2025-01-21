package com.mos.inventory.service.session;

import com.mos.inventory.dto.EquipTransactionMessage;
import com.mos.inventory.dto.EquipTransactionResult;
import com.mos.inventory.entity.RentalRegister;
import com.mos.inventory.entity.ReservationRegister;
import com.mos.inventory.presistance.RentalDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.service.UserContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EquipTxnValidatorImpl implements EquipTxnValidator {
    public static final int RECRUIT_MAX_RESERVATIONS = 1;
    public static final int MOSOWICZ_MAX_RESERVATIONS = 5;
    public static final int ZARZAD_ADMIN_MAX_RESERVATIONS = 10;
    private static final int MOSOWICZ_MAX_RENTALS = 3;
    public static final int ZARZAD_ADMIN_MAX_RENTALS = 5;

    private final UserContextHolder userContextHolder;
    private final ReservationDAO reservationDAO;
    private final RentalDAO rentalDAO;

    public EquipTxnValidatorImpl(UserContextHolder userContextHolder, ReservationDAO reservationDAO, RentalDAO rentalDAO) {
        this.userContextHolder = userContextHolder;
        this.reservationDAO = reservationDAO;
        this.rentalDAO = rentalDAO;
    }

    @Override
    public Optional<EquipTransactionResult> validateInputData(Set<UUID> equipmentIDs, Date startDate, Date endDate, String transactionType) {
        Optional<EquipTransactionResult> validationResult;

        validationResult = checkIfUserContextIsPresent();
        if (validationResult.isPresent()) {
            return validationResult;
        }

        validationResult = equipmentValidation(equipmentIDs);
        if (validationResult.isPresent()) {
            return validationResult;
        }

        validationResult = dateValidation(startDate, endDate);
        if (validationResult.isPresent()) {
            return validationResult;
        }

        validationResult = transactionPolicyValidation(transactionType);
        return validationResult;

    }

    @Override
    public Optional<EquipTransactionResult> checkIfUserContextIsPresent() {
        if (userContextHolder.get() != null) {
            return Optional.empty();
        }

        return Optional.of(new EquipTransactionResult(EquipTransactionMessage.USER_ERROR));

    }

    @Override
    public Optional<EquipTransactionResult> equipmentValidation(Set<UUID> equipmentIDs) {
        if(equipmentIDs.isEmpty()) {
            return Optional.of(new EquipTransactionResult(EquipTransactionMessage.EQUIPMENT_ID_LIST_ERROR));
        }
        return Optional.empty();
    }

    @Override
    public Optional<EquipTransactionResult> dateValidation(Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        if (startDate.before(yesterday) || endDate.before(yesterday)) {
            return Optional.of(new EquipTransactionResult((EquipTransactionMessage.DATE_ERROR)));
        }

        if(endDate.before(startDate)) {
            return Optional.of(new EquipTransactionResult((EquipTransactionMessage.DATE_ERROR)));
        }

        return Optional.empty();
    }

    @Override
    public Optional<EquipTransactionResult> transactionPolicyValidation(String transactionType) {
        if (transactionType.equals("RESERVATION")) {
            return reservationPolicyValidation();
        } else if (transactionType.equals("RENTAL")){
            return rentalPolicyValidation();
        } else {
            return Optional.of(new EquipTransactionResult(EquipTransactionMessage.INVALID_TRANSACTION_TYPE));
        }

    }


    private Optional<EquipTransactionResult> reservationPolicyValidation() {
        List<ReservationRegister> reservations = reservationDAO.findReservationsBy(userContextHolder.get().getUserID());

        int maxReservations = getMaximumNumberOfReservations(userContextHolder.get().getUserRoleName());
        if (reservations.size() >= maxReservations) {
            return Optional.of(new EquipTransactionResult(EquipTransactionMessage.RESERVATION_ERROR));
        }
        return Optional.empty();
    }

    private Optional<EquipTransactionResult> rentalPolicyValidation() {
        List<RentalRegister> rentals = rentalDAO.findRentalsFor(userContextHolder.get().getUserID());

        int maxReservations = getMaximumNumberOfRentals(userContextHolder.get().getUserRoleName());
        if (rentals.size() >= maxReservations) {
            return Optional.of(new EquipTransactionResult(EquipTransactionMessage.RENTAL_ERROR));
        }
        return Optional.empty();
    }

    private int getMaximumNumberOfReservations(String userRoleName) {
        switch (userRoleName) {
            case "Rekrut":
                return RECRUIT_MAX_RESERVATIONS;
            case "Mosowicz":
                return MOSOWICZ_MAX_RESERVATIONS;
            case "Zarzad":
            case "Admin":
                return ZARZAD_ADMIN_MAX_RESERVATIONS;
            default:
                return 0;
        }
    }
    private int getMaximumNumberOfRentals(String userRoleName) {
        switch (userRoleName) {
            case "Mosowicz":
                return MOSOWICZ_MAX_RENTALS;
            case "Zarzad":
            case "Admin":
                return ZARZAD_ADMIN_MAX_RENTALS;
            default:
                return 0;
        }
    }
}
