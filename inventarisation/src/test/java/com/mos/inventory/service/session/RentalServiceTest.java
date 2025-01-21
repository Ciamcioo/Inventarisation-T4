package com.mos.inventory.service.session;

import com.mos.inventory.dto.*;
import com.mos.inventory.entity.RentalRegister;
import com.mos.inventory.presistance.*;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.User;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentalServiceTest {
    private static final Logger log = LoggerFactory.getLogger(RentalServiceTest.class);
    final String rental = "RENTAL";
    RentalService rentalService;

    UserContextHolder userContextHolder;
    EquipTxnValidator validator;
    EquipmentDAO equipmentDAO;
    ReservationDAO reservationDAO;
    RentalDAO rentalDAO;

    UserContext user;
    Set<UUID> equipmentIDs;
    Date startDate;
    Date endDate;
    List<Equipment> equipment;

    @BeforeEach
    void setup() {
        user = new UserContext(new User(), "dummy@dummy.com");
        user.getUser().setId(UUID.randomUUID());
        user.setUserRoleDescription("Mosowicz");

        equipmentIDs = new HashSet<>();
        equipmentIDs.add(UUID.randomUUID());
        startDate = new Date();
        endDate = new Date();

        equipment = new ArrayList<>();
        Equipment eq = new Equipment();
        eq.setQuantity(2);
        equipment.add(eq);
        equipment.add(eq);
        equipment.add(eq);

        userContextHolder = mock(UserContextHolder.class);
        validator = mock(EquipTxnValidator.class);
        equipmentDAO = mock(EquipmentDAO.class);
        reservationDAO = mock(ReservationDAO.class);
        rentalDAO = mock(RentalDAO.class);

        when(userContextHolder.get()).thenReturn(user);

        rentalService = new EquipTxnService(equipmentDAO,
                                            reservationDAO,
                                            userContextHolder,
                                            validator,
                                            rentalDAO);
    }

    @Test
    void createRentalWithInvalidEquipmentData_ValidatorShouldReturnNotEmptyOptional() {
        EquipTransactionResult result = new EquipTransactionResult(EquipTransactionMessage.EQUIPMENT_ID_LIST_ERROR);
        when(validator.validateInputData(equipmentIDs, startDate, endDate,rental))
                .thenReturn(Optional.of(result));

        assertEquals(result, rentalService.createRental(equipmentIDs, startDate, endDate, rental));
        verify(validator).validateInputData(equipmentIDs, startDate, endDate, rental);
    }

   @Test
    void createRentalWithInvalidDateData_ValidatorShouldReturnNotEmptyOptional() {
        EquipTransactionResult result = new EquipTransactionResult(EquipTransactionMessage.DATE_ERROR);
        when(validator.validateInputData(equipmentIDs, startDate, endDate, rental))
                .thenReturn(Optional.of(result));

        assertEquals(result, rentalService.createRental(equipmentIDs, startDate, endDate, rental));
        verify(validator).validateInputData(equipmentIDs, startDate, endDate, rental);
    }

    @Test
    void createRentalWithValidData_createRentalShouldReturnSuccessfulRentalMessage() {
        EquipTransactionResult result = new EquipTransactionResult(EquipTransactionMessage.SUCCESSFUL_RENTAL);
        when(validator.validateInputData(equipmentIDs, startDate, endDate, rental))
                .thenReturn(Optional.empty());

        assertEquals(result, rentalService.createRental(equipmentIDs, startDate, endDate, rental));
        verify(validator).validateInputData(equipmentIDs, startDate, endDate, rental);
    }

    @Test
    void createRentalWithValidData_createRentalShouldInvokeFindAllMethod() {
        when(validator.validateInputData(equipmentIDs, startDate, endDate, rental))
                .thenReturn(Optional.empty());

        rentalService.createRental(equipmentIDs, startDate, endDate, rental);
        verify(equipmentDAO).findAllBy(equipmentIDs);
    }

    @Test
    void createRentalMethodForValidData_ShouldInvokeUpdateMethodThreeEquipments() {
        when(validator.validateInputData(equipmentIDs, startDate, endDate, rental))
                .thenReturn(Optional.empty());
        when(equipmentDAO.findAllBy(equipmentIDs)).thenReturn(equipment);

        rentalService.createRental(equipmentIDs, startDate, endDate, rental);

        verify(equipmentDAO, times(3)).updateEquipment(any(Equipment.class));
    }

    @Test
    void creteRentalShouldFilterOutEquipmentWithQuantityOneOrZero_ShouldInvokeUpdateMethodTwoEquipments() {
        Equipment eqWithZeroQuantity = new Equipment();
        eqWithZeroQuantity.setQuantity(0);
        equipment.set(0, eqWithZeroQuantity);

        when(validator.validateInputData(equipmentIDs, startDate, endDate, rental))
                .thenReturn(Optional.empty());
        when(equipmentDAO.findAllBy(equipmentIDs)).thenReturn(equipment);

        rentalService.createRental(equipmentIDs, startDate, endDate, rental);

        verify(equipmentDAO, times(2)).updateEquipment(any(Equipment.class));
    }

    @Test
    void crateRentalShouldAddNewRentalToRentalTable_ShouldInvokeInsertRentalOfRentalDAO() {
        when(validator.validateInputData(equipmentIDs, startDate, endDate, rental))
                .thenReturn(Optional.empty());
        when(equipmentDAO.findAllBy(equipmentIDs)).thenReturn(equipment);

        rentalService.createRental(equipmentIDs, startDate, endDate, rental);

        verify(rentalDAO).insertRental(any(RentalRegister.class));
    }

    @Test
    void returnRentalShouldReturnReturnResultObject() {
        assertInstanceOf(EquipTransactionResult.class, rentalService.returnRental(UUID.randomUUID()));
    }

    @Test
    void returnRentalForValidUUID_ShouldReturnReturnResultWithSuccessfulMessage() {
        when(rentalDAO.checkIfContainsRental(any(UUID.class))).thenReturn(true);
        assertEquals(EquipTransactionMessage.SUCCESSFUL_RETURN, rentalService.returnRental(UUID.randomUUID()).getMessage());
    }

    @Test
    void returnRentalForInvalidUUID_ShouldReturnMessageShouldBeInvalidReservationID() {
        assertEquals(EquipTransactionMessage.INVALID_RENTAL_ID, rentalService.returnRental(null).getMessage());
    }

    @Test
    void returnRentalForNotLoggedUser_ShouldReturnNotLoggedUserMessage() {
        when(validator.checkIfUserContextIsPresent()).thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.USER_ERROR)));

        assertEquals(EquipTransactionMessage.USER_ERROR, rentalService.returnRental(UUID.randomUUID()).getMessage());
    }

    @Test
    void returnRentalInvokesCheckIfContainsRental_ShouldCallOnceAndTransactionResultMessageShouldBeSuccessful() {
        when(rentalDAO.checkIfContainsRental(any(UUID.class))).thenReturn(true);
        assertEquals(EquipTransactionMessage.SUCCESSFUL_RETURN, rentalService.returnRental(UUID.randomUUID()).getMessage());
        verify(rentalDAO).checkIfContainsRental(any(UUID.class));
    }

    @Test
    void returnRentalInvokesCheckIfContainsRental_ShouldCallOnceAndTransactionShouldBeInvalidRentalID() {
        when(rentalDAO.checkIfContainsRental(any(UUID.class))).thenReturn(false);
        assertEquals(EquipTransactionMessage.INVALID_RENTAL_ID, rentalService.returnRental(UUID.randomUUID()).getMessage());
        verify(rentalDAO).checkIfContainsRental(any(UUID.class));
    }

    @Test
    void checkIfInvokesRemoveRental_ShouldInvokeOnce() {
        when(rentalDAO.checkIfContainsRental(any(UUID.class))).thenReturn(true);
        assertEquals(EquipTransactionMessage.SUCCESSFUL_RETURN, rentalService.returnRental(UUID.randomUUID()).getMessage());

        verify(rentalDAO).removeRental(any(UUID.class));

    }




}