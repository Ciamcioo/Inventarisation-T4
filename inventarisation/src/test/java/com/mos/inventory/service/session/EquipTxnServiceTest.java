package com.mos.inventory.service.session;

import com.mos.inventory.presistance.RentalDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EquipTxnServiceTest {
    UserContextHolder userContextHolder;
    EquipTxnValidator validator;
    ReservationDAO reservationDAO;
    RentalDAO rentalDAO;

    int maximumNumberOfReservationForRecruit = 1;
    int maximumNumberOfReservationForMosowicz= 5;
    int maximumNumberOfReservationForZarzadAndAdmin= 10;
    int maximumNumberOfReservationForUnknownRole = 0;


    @BeforeEach
    void setup() {
        userContextHolder = mock(UserContextHolder.class);
        reservationDAO = mock(ReservationDAO.class);
        rentalDAO = mock(RentalDAO.class);
        validator = new EquipTxnValidatorImpl(userContextHolder, reservationDAO,rentalDAO);
    }

    @Test
    void maximumNumberOfReservationsForRecruit_ShouldReturnOne() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMaximumNumberOfReservations = EquipTxnValidatorImpl.class.getDeclaredMethod("getMaximumNumberOfReservations", String.class);
        getMaximumNumberOfReservations.setAccessible(true);

        int maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(validator, "Rekrut");

        assertEquals(maximumNumberOfReservationForRecruit, maximumReservationNumber);
    }

    @Test
    void maximumNumberOfReservationsForMosowicz_ShouldReturnFive() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMaximumNumberOfReservations = EquipTxnValidatorImpl.class.getDeclaredMethod("getMaximumNumberOfReservations", String.class);
        getMaximumNumberOfReservations.setAccessible(true);

        int maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(validator, "Mosowicz");

        assertEquals(maximumNumberOfReservationForMosowicz, maximumReservationNumber);
    }

    @Test
    void maximumNumberOfReservationsForZarzadAndAdmin_ShouldReturnTen() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMaximumNumberOfReservations = EquipTxnValidatorImpl.class.getDeclaredMethod("getMaximumNumberOfReservations", String.class);
        getMaximumNumberOfReservations.setAccessible(true);

        int maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(validator, "Zarzad");

        assertEquals(maximumNumberOfReservationForZarzadAndAdmin, maximumReservationNumber);

        maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(validator, "Admin");

        assertEquals(maximumNumberOfReservationForZarzadAndAdmin, maximumReservationNumber);
    }

    @Test
    void maximumNumberOfReservationsForUnknownRole_ShouldReturnZero() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMaximumNumberOfReservations = EquipTxnValidatorImpl.class.getDeclaredMethod("getMaximumNumberOfReservations", String.class);
        getMaximumNumberOfReservations.setAccessible(true);

        int maximumReservationNumber = (int) getMaximumNumberOfReservations.invoke(validator, "abc");

        assertEquals(maximumNumberOfReservationForUnknownRole, maximumReservationNumber);
    }





}
