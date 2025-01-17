package com.mos.inventory.service.session;

import com.mos.inventory.dto.ReservationMessage;
import com.mos.inventory.dto.ReservationResult;
import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.ReservationRegister;
import com.mos.inventory.entity.Role;
import com.mos.inventory.entity.User;
import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    ReservationService reservationService;

    UserContextHolder userContextHolder;
    EquipmentDAO equipmentDAO;
    ReservationDAO reservationDAO;
    User user;

    Set<UUID> correctEquipmentID = new HashSet<>();
    Date startDate = new Date();
    Date endDate = new Date();

    ReservationResult userErrorReservationResult = new ReservationResult(ReservationMessage.USER_ERROR);
    ReservationResult equipmentListErrorReservationResult = new ReservationResult(ReservationMessage.EQUIPMENT_ID_LIST_ERROR);
    ReservationResult successfulReservationResult = new ReservationResult(ReservationMessage.SUCCESSFUL_RESERVATION);
    ReservationResult dataErrorReservationResult = new ReservationResult(ReservationMessage.DATE_ERROR);
    ReservationResult reservationErrorReservationResult = new ReservationResult(ReservationMessage.RESERVATION_ERROR);



    @BeforeEach
    void setup() {
        userContextHolder = mock(UserContextHolder.class);
        equipmentDAO = mock(EquipmentDAO.class);
        reservationDAO = mock(ReservationDAO.class);
        reservationService = new ReservationServiceImpl(equipmentDAO, reservationDAO, userContextHolder);

        user = new User();
        user.setId(UUID.randomUUID());
        Role role = new Role();
        role.setName("Rekrut");
        user.setRole(role);
        reservationService.setUserContextForService(new UserContext(user, "dummy@dummy.com"));

        correctEquipmentID.add(UUID.randomUUID());
        userContextHolder.set(null);
        correctEquipmentID.add(UUID.randomUUID());

    }

    @Test
    void userContextIsNull_ReservationResultNotSuccessful() {
        reservationService.setUserContextForService(null);
        userContextHolder.set(null);

        assertEquals(userErrorReservationResult,
                     reservationService.createReservation(correctEquipmentID, startDate, endDate));

    }

    @Test
    void userContextIsNotNullInUserContextHolder_ReservationResultSuccessful() {
        reservationService.setUserContextForService(null);
        userContextHolder.set(new UserContext(user, "dummy@dummy.com"));

        when(userContextHolder.get()).thenReturn(new UserContext(user, "dummy@dummy.com"));

        assertEquals(successfulReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate));
    }

    @Test
    void userContextInReservationServiceNotNull_ReservationResultSuccessful() {
        assertEquals(successfulReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate));
    }

    @Test
    void equipmentIDListIsEmpty_ReservationResultContainsEquipmentIDError() {
        assertEquals(equipmentListErrorReservationResult,
                reservationService.createReservation(new HashSet<>(), startDate, endDate));
    }

    @Test
    void startDateIsInThePast_ReservationResultContainsDataError() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date startDateFromPast = calendar.getTime();


        assertEquals(dataErrorReservationResult,
               reservationService.createReservation(correctEquipmentID, startDateFromPast, endDate));
    }

    @Test
    void endDateIsInThePast_ReservationResultContainsDataError() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date endDateFromPast = calendar.getTime();


        assertEquals(dataErrorReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDateFromPast));
    }

    @Test
    void endDateIsBeforeStartDate_ReservationResultContainsDataError() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +30);
        startDate = calendar.getTime();
        calendar.add(Calendar.DATE, -15);
        endDate = calendar.getTime();


        assertEquals(dataErrorReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate));
    }

    @Test
    void sessionUserWhoIsRecruitCurrentlyHasReservations_ReservationResultWillReturnUserError() {
        List<ReservationRegister> userReservations = new ArrayList<>();
        userReservations.add(new ReservationRegister());
        when(reservationDAO.findReservationsBy(any(UUID.class))).thenReturn(userReservations);

        assertEquals(reservationErrorReservationResult,
               reservationService.createReservation(correctEquipmentID, startDate, endDate));
    }

    @Test
    void sessionUserWhoIsRecruitCurrentlyDoesNotHaveReservations_ReservationServiceWillReturnReservationSuccessful() {
        when(reservationDAO.findReservationsBy(any(UUID.class))).thenReturn(new ArrayList<>());

        assertEquals(successfulReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate));
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

        assertEquals(successfulReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate));

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

        assertEquals(successfulReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate));

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

        assertEquals(successfulReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate));

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

        assertEquals(successfulReservationResult,
                reservationService.createReservation(correctEquipmentID, startDate, endDate));

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

        reservationService.createReservation(correctEquipmentID, startDate, endDate);

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

        reservationService.createReservation(correctEquipmentID, startDate, endDate);

        verify(reservationDAO, times(1)).insertReservation(reservationRegister);
    }


}
