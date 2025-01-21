package com.mos.inventory.service.session;

import com.mos.inventory.dto.*;
import com.mos.inventory.entity.*;
import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.RentalDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.service.UserContextHolder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    ReservationService reservationService;

    UserContextHolder userContextHolder;
    EquipTxnValidator validator;
    EquipmentDAO equipmentDAO;
    ReservationDAO reservationDAO;
    RentalDAO rentalDAO;
    User user;
    String transactionType = "RESERVATION";

    Set<UUID> correctEquipmentID = new HashSet<>();
    Date startDate = new Date();
    Date endDate = new Date();

    EquipTransactionResult userErrorEquipTransactionResult = new EquipTransactionResult(EquipTransactionMessage.USER_ERROR);
    EquipTransactionResult equipmentListErrorEquipTransactionResult = new EquipTransactionResult(EquipTransactionMessage.EQUIPMENT_ID_LIST_ERROR);
    EquipTransactionResult successfulEquipTransactionResult = new EquipTransactionResult(EquipTransactionMessage.SUCCESSFUL_RESERVATION);
    EquipTransactionResult dataErrorEquipTransactionResult = new EquipTransactionResult(EquipTransactionMessage.DATE_ERROR);
    EquipTransactionResult reservationErrorEquipTransactionResult = new EquipTransactionResult(EquipTransactionMessage.RESERVATION_ERROR);



    @BeforeEach
    void setup() {
        userContextHolder = mock(UserContextHolder.class);
        equipmentDAO = mock(EquipmentDAO.class);
        reservationDAO = mock(ReservationDAO.class);
        validator = mock(EquipTxnValidator.class);
        reservationService = new EquipTxnService(equipmentDAO, reservationDAO, userContextHolder, validator, rentalDAO);

        user = new User();
        user.setId(UUID.randomUUID());
        Role role = new Role();
        role.setName("Rekrut");
        user.setRole(role);
        reservationService.setUserContextForService(new UserContext(user, "dummy@dummy.com"));

        correctEquipmentID.add(UUID.randomUUID());
        userContextHolder.set(null);
        correctEquipmentID.add(UUID.randomUUID());

        when(userContextHolder.get()).thenReturn(new UserContext());

    }

    @Test
    void userContextIsNull_ReservationResultNotSuccessful() {
        reservationService.setUserContextForService(null);
        userContextHolder.set(null);
        when(validator.validateInputData(correctEquipmentID, startDate, endDate, transactionType))
                .thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.USER_ERROR)));

        assertEquals(userErrorEquipTransactionResult,
                     reservationService.createReservation(correctEquipmentID, startDate, endDate, transactionType));

    }

    @Test
    void userContextIsNotNullInUserContextHolder_ReservationResultSuccessful() {
        reservationService.setUserContextForService(null);
        userContextHolder.set(new UserContext(user, "dummy@dummy.com"));

        when(userContextHolder.get()).thenReturn(new UserContext(user, "dummy@dummy.com"));
        when(validator.validateInputData(correctEquipmentID,startDate,endDate,transactionType))
                .thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.SUCCESSFUL_RESERVATION)));

        assertEquals(successfulEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));
    }

    @Test
    void userContextInReservationServiceNotNull_ReservationResultSuccessful() {
        assertEquals(successfulEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));
    }

    @Test
    void equipmentIDListIsEmpty_ReservationResultContainsEquipmentIDError() {
        when(validator.validateInputData(new HashSet<>(),startDate,endDate,transactionType)).thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.EQUIPMENT_ID_LIST_ERROR)));
        assertEquals(equipmentListErrorEquipTransactionResult,
                reservationService.createReservation(new HashSet<>(), startDate, endDate,transactionType));
    }

    @Test
    void startDateIsInThePast_ReservationResultContainsDataError() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date startDateFromPast = calendar.getTime();

        when(validator.validateInputData(correctEquipmentID,startDateFromPast,endDate,transactionType)).thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.DATE_ERROR)));


        assertEquals(dataErrorEquipTransactionResult,
               reservationService.createReservation(correctEquipmentID, startDateFromPast, endDate,transactionType));
    }

    @Test
    void endDateIsInThePast_ReservationResultContainsDataError() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date endDateFromPast = calendar.getTime();

        when(validator.validateInputData(correctEquipmentID,startDate,endDateFromPast,transactionType)).thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.DATE_ERROR)));

        assertEquals(dataErrorEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDateFromPast,transactionType));
    }

    @Test
    void endDateIsBeforeStartDate_ReservationResultContainsDataError() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +30);
        startDate = calendar.getTime();
        calendar.add(Calendar.DATE, -15);
        endDate = calendar.getTime();

        when(validator.validateInputData(correctEquipmentID,startDate,endDate,transactionType)).thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.DATE_ERROR)));

        assertEquals(dataErrorEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));
    }

    @Test
    void sessionUserWhoIsRecruitCurrentlyHasReservations_ReservationResultWillReturnUserError() {
        List<ReservationRegister> userReservations = new ArrayList<>();
        userReservations.add(new ReservationRegister());
        when(reservationDAO.findReservationsBy(any(UUID.class))).thenReturn(userReservations);
        when(validator.validateInputData(correctEquipmentID, startDate, endDate,transactionType)).thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.RESERVATION_ERROR)));

        assertEquals(reservationErrorEquipTransactionResult,
               reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));
    }

    @Test
    void sessionUserWhoIsRecruitCurrentlyDoesNotHaveReservations_ReservationServiceWillReturnReservationSuccessful() {
        when(reservationDAO.findReservationsBy(any(UUID.class))).thenReturn(new ArrayList<>());

        assertEquals(successfulEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));
    }

    @Test
    void decrementTheQuantityValueWhenReservingItemAndUpdateItInDataBase() {
        int startQuantity = 10;
        Equipment eq1 = new Equipment();
        eq1.setQuantity(startQuantity);

        startQuantity = 5;
        Equipment eq2 = new Equipment();
        eq2.setQuantity(startQuantity);


        when(equipmentDAO.findAllBy(correctEquipmentID)).thenReturn(List.of(eq1, eq2));

        assertEquals(successfulEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));

        startQuantity = 10;
        assertEquals(startQuantity - 1, eq1.getQuantity());

        startQuantity = 5;
        assertEquals(startQuantity - 1, eq2.getQuantity());

    }

    @Test
    void decrementTheValueWhenQuantityIsOneOrMore_ShouldUpdateJustOneObject() {
        int startQuantity = 10;
        Equipment eq1 = new Equipment();
        eq1.setQuantity(startQuantity);

        startQuantity = 0;
        Equipment eq2 = new Equipment();
        eq2.setQuantity(startQuantity);
        when(equipmentDAO.findAllBy(correctEquipmentID)).thenReturn(List.of(eq1, eq2));

        assertEquals(successfulEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));

        startQuantity = 10;
        assertEquals(startQuantity - 1, eq1.getQuantity());

        startQuantity = 0;
        assertEquals(startQuantity , eq2.getQuantity());
    }

    @Test
    void verifyIfUpdateEquipmentWasCalledTwoTimesAfterDecrementing_ShouldCallTwoTimes() {
        int startQuantity = 10;
        Equipment eq1 = new Equipment();
        eq1.setQuantity(startQuantity);

        startQuantity = 5;
        Equipment eq2 = new Equipment();
        eq2.setQuantity(startQuantity);


        when(equipmentDAO.findAllBy(correctEquipmentID)).thenReturn(List.of(eq1, eq2));

        assertEquals(successfulEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));

        startQuantity = 10;
        assertEquals(startQuantity - 1, eq1.getQuantity());

        startQuantity = 5;
        assertEquals(startQuantity - 1, eq2.getQuantity());

        verify(equipmentDAO, times(2)).updateEquipment(any(Equipment.class));
    }

    @Test
    void decrementTheQuantityValueOfEquipment_ShouldNotDecrement() {
        int startQuantity = 0;
        Equipment eq1 = new Equipment();
        eq1.setQuantity(startQuantity);

        Equipment eq2 = new Equipment();
        eq2.setQuantity(startQuantity);
        when(equipmentDAO.findAllBy(correctEquipmentID)).thenReturn(List.of(eq1, eq2));

        assertEquals(successfulEquipTransactionResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType));

        assertEquals(startQuantity , eq1.getQuantity());

        assertEquals(startQuantity , eq2.getQuantity());
    }

    @Test
    void insertNewReservationRecords_ShouldBeCalledForAllEquipmentObject() {
        int startQuantity = 9;
        Equipment eq1 = new Equipment();
        eq1.setQuantity(startQuantity);
        Equipment eq2 = new Equipment();
        eq2.setQuantity(startQuantity);

        when(equipmentDAO.findAllBy(correctEquipmentID)).thenReturn(List.of(eq1, eq2));

        reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType);

        verify(reservationDAO, times(1)).insertReservation(any(ReservationRegister.class));
    }

    @Test
    void insertNewReservationForSpecifiedReservationData_ReservationRegisterShouldContainSpecifiedData() {
        int startQuantity = 9;
        Equipment eq1 = new Equipment();
        eq1.setQuantity(startQuantity);
        Equipment eq2 = new Equipment();
        eq2.setQuantity(startQuantity);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 3);
        Date timeout = calendar.getTime();


        List<Equipment> equipment = List.of(eq1, eq2);
        ReservationRegister reservationRegister;
        reservationRegister = new ReservationRegister(user, equipment, timeout, startDate, endDate);

        when(equipmentDAO.findAllBy(correctEquipmentID)).thenReturn(List.of(eq1, eq2));

        reservationService.createReservation(correctEquipmentID, startDate, endDate,transactionType);

        verify(reservationDAO, times(1)).insertReservation(any(ReservationRegister.class));
    }

    @Test
    void equipTransactionResultIfValidEntryData_ShouldReturnSuccessfulEquipTxnResult() {
        when(validator.validateInputData(correctEquipmentID, startDate, endDate,transactionType)).thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.SUCCESSFUL_RESERVATION)));


    }


}
