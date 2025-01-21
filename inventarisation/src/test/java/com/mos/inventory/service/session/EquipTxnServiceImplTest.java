package com.mos.inventory.service.session;

import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.RentalDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

public class EquipTxnServiceImplTest {
    EquipTxnService equipTxnService;

    EquipmentDAO equipmentDAO;
    ReservationDAO reservationDAO;
    RentalDAO rentalDAO;
    UserContextHolder userContextHolder;
    EquipTxnValidator validator;
    List<Equipment> eqList;


    @BeforeEach
    void setup() {
        equipmentDAO = mock(EquipmentDAO.class);
        equipTxnService = new EquipTxnService(equipmentDAO, reservationDAO, userContextHolder, validator, rentalDAO);

        eqList = new ArrayList<>();
        Equipment validEquipment = new Equipment();
        Equipment invalidEquipment = new Equipment();
        validEquipment.setQuantity(5);

        eqList.add(validEquipment);
        eqList.add(validEquipment);

        eqList.add(invalidEquipment);
    }

    @Test
    void filterOutTheEquipmentList_ShouldReturnTwoElementsOutOfThreeEntry() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method filterOutUnavailable = EquipTxnService.class.getDeclaredMethod( "filterOutUnavailable", List.class);
        filterOutUnavailable.setAccessible(true);

        eqList = (List<Equipment>) filterOutUnavailable.invoke(equipTxnService, eqList);

        assertEquals(2, eqList.size());
    }
}
