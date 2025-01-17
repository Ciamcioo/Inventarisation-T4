package com.mos.inventory.service.session;

import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.presistance.ReservationDAOImpl;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ReservationServiceImplTest {
    ReservationServiceImpl reservationService;
    UserContextHolder userContextHolder;
    ReservationDAO reservationDAO;
    EquipmentDAO equipmentDAO;

    int maximumNumberOfReservationForRecruit = 1;
    int maximumNumberOfReservationForMosowicz= 5;
    int maximumNumberOfReservationForZarzadAndAdmin= 10;
    int maximumNumberOfReservationForUnknownRole = 0;


    @BeforeEach
    void setup() {
        userContextHolder = mock(UserContextHolder.class);
        reservationDAO = mock(ReservationDAO.class);
        equipmentDAO = mock(EquipmentDAO.class);
        reservationService = new ReservationServiceImpl(equipmentDAO, reservationDAO, userContextHolder);
    }

    @Test
    void maximumNumberOfReservationsForRecruit_ShouldReturnOne() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMaximumNumberOfReservations = ReservationServiceImpl.class.getDeclaredMethod("getMaximumNumberOfReservations", String.class);
        getMaximumNumberOfReservations.setAccessible(true);

        int maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(reservationService, "Rekrut");

        assertEquals(maximumNumberOfReservationForRecruit, maximumReservationNumber);
    }

    @Test
    void maximumNumberOfReservationsForMosowicz_ShouldReturnFive() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMaximumNumberOfReservations = ReservationServiceImpl.class.getDeclaredMethod("getMaximumNumberOfReservations", String.class);
        getMaximumNumberOfReservations.setAccessible(true);

        int maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(reservationService, "Mosowicz");

        assertEquals(maximumNumberOfReservationForMosowicz, maximumReservationNumber);
    }

    @Test
    void maximumNumberOfReservationsForZarzadAndAdmin_ShouldReturnTen() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMaximumNumberOfReservations = ReservationServiceImpl.class.getDeclaredMethod("getMaximumNumberOfReservations", String.class);
        getMaximumNumberOfReservations.setAccessible(true);

        int maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(reservationService, "Zarzad");

        assertEquals(maximumNumberOfReservationForZarzadAndAdmin, maximumReservationNumber);

        maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(reservationService, "Admin");

        assertEquals(maximumNumberOfReservationForZarzadAndAdmin, maximumReservationNumber);
    }

    @Test
    void maximumNumberOfReservationsForUnknownRole_ShouldReturnZero() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMaximumNumberOfReservations = ReservationServiceImpl.class.getDeclaredMethod("getMaximumNumberOfReservations", String.class);
        getMaximumNumberOfReservations.setAccessible(true);

        int maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(reservationService, "abc");

        assertEquals(maximumNumberOfReservationForUnknownRole, maximumReservationNumber);
    }

}
