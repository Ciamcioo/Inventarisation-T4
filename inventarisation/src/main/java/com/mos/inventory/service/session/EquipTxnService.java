package com.mos.inventory.service.session;

import com.mos.inventory.dto.*;
import com.mos.inventory.entity.*;
import com.mos.inventory.presistance.*;
import com.mos.inventory.service.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EquipTxnService implements ReservationService, RentalService{
    private final UserContextHolder userContextHolder;
    private final EquipTxnValidator validator;
    private final EquipmentDAO equipmentDAO;
    private final ReservationDAO reservationDAO;
    private final RentalDAO rentalDAO;


    private UserContext userContext;

    public EquipTxnService(EquipmentDAO equipmentDAO,
                           ReservationDAO reservationDAO,
                           UserContextHolder userContextHolder,
                           EquipTxnValidator validator,
                           RentalDAO rentalDAO) {
        this.equipmentDAO = equipmentDAO;
        this.reservationDAO = reservationDAO;
        this.userContextHolder = userContextHolder;
        this.validator = validator;
        this.rentalDAO = rentalDAO;
    }

    @Override
    @Transactional
    public EquipTransactionResult createReservation(Set<UUID> equipmentIDs, Date startDate, Date endDate, String transactionType) {
        Optional<EquipTransactionResult> validationResult;
        validationResult = validator.validateInputData(equipmentIDs, startDate, endDate, transactionType);
        if (validationResult.isPresent()) {
            return validationResult.get();
        }

        List<Equipment> reservationEquipment;
        reservationEquipment = equipmentDAO.findAllBy(equipmentIDs);
        reservationEquipment = filterOutUnavailable(reservationEquipment);
        decreaseQuantityOfEquipmentInDatabase(reservationEquipment);


        Date timeout = getTimeout(endDate);

        ReservationRegister reservationRegister;
        reservationRegister = new ReservationRegister(userContextHolder.get().getUser(),
                                                      reservationEquipment,
                                                      timeout,
                                                      startDate,
                                                      endDate);
        reservationDAO.insertReservation(reservationRegister);

        return validationResult.orElseGet(() -> new EquipTransactionResult(EquipTransactionMessage.SUCCESSFUL_RESERVATION));

    }

    @Override
    @Transactional
    public EquipTransactionResult createRental(Set<UUID> equipmentIDs, Date startDate, Date endDate, String transactionType) {
        Optional<EquipTransactionResult> validationResult = validator.validateInputData(equipmentIDs,startDate,endDate, transactionType);
        if (validationResult.isPresent()) {
            return validationResult.get();
        }

        List<Equipment> equipment = equipmentDAO.findAllBy(equipmentIDs);
        equipment = filterOutUnavailable(equipment);
        decreaseQuantityOfEquipmentInDatabase(equipment);

        RentalRegister newRental = new RentalRegister(userContextHolder.get().getUser(),
                                                      equipment,
                                                      startDate,
                                                      endDate);
        rentalDAO.insertRental(newRental);

        return new EquipTransactionResult(EquipTransactionMessage.SUCCESSFUL_RENTAL);

    }

    @Override
    public EquipTransactionResult returnRental(UUID rentalID) {
        Optional<EquipTransactionResult> validationResult = validator.checkIfUserContextIsPresent();

        if (validationResult.isPresent()) {
            return validationResult.get();
        }

        boolean isAvailableRental = rentalDAO.checkIfContainsRental(rentalID);
        if (!isAvailableRental) {
            return new EquipTransactionResult(EquipTransactionMessage.INVALID_RENTAL_ID);
        }

        rentalDAO.removeRental(rentalID);

        return new EquipTransactionResult(EquipTransactionMessage.SUCCESSFUL_RETURN);
    }

    private static Date getTimeout(Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 3);
        return calendar.getTime();
    }

    private List<Equipment> filterOutUnavailable(List<Equipment> equipment) {
        return equipment.stream()
               .filter(eq -> eq.getQuantity() >= 1)
               .collect(Collectors.toList());
    }

    private void decreaseQuantityOfEquipmentInDatabase(List<Equipment> equipment) {
        equipment.forEach(eq -> {
            eq.setQuantity(eq.getQuantity() - 1);
            equipmentDAO.updateEquipment(eq);
        });
    }

    private HashSet<UUID> convertEquipmentListToEquipmentUuidSet(List<Equipment> equipment) {
        return new HashSet<>(equipment.stream().
                             map(Equipment::getId).
                             collect(Collectors.toList()));
    }

    @Override
    public void setUserContextForService(UserContext userContext) {
        this.userContext =userContext;
    }
}
