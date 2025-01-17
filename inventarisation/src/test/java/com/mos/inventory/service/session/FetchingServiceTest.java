package com.mos.inventory.service.session;

import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.*;
import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.LocationDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.presistance.StatusDAO;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FetchingServiceTest {
    FetchingService fetchingService;
    ReservationDAO reservationDAO;
    StatusDAO statusDAO;
    LocationDAO locationDAO;
    EquipmentDAO equipmentDAO;
    UserContextHolder userContextHolder;

    List<Equipment> equipmentList;
    List<Location> locationList;
    List<Status> statusList;
    List<ReservationRegister> reservationRegisterList;
    @BeforeEach
    void setup() {
        reservationRegisterList = new ArrayList<>();

        reservationRegisterList.add(new ReservationRegister());
        reservationRegisterList.add(new ReservationRegister());
        reservationRegisterList.add(new ReservationRegister());

        equipmentList = new ArrayList<>();

        equipmentList.add(new Equipment());
        equipmentList.add(new Equipment());
        equipmentList.add(new Equipment());

        locationList = new ArrayList<>();

        locationList.add(new Location());
        locationList.add(new Location());
        locationList.add(new Location());

        statusList = new ArrayList<>();

        statusList.add(new Status());
        statusList.add(new Status());
        statusList.add(new Status());

        reservationDAO = mock(ReservationDAO.class);
        statusDAO = mock(StatusDAO.class);
        locationDAO = mock(LocationDAO.class);
        equipmentDAO = mock(EquipmentDAO.class);
        userContextHolder = mock(UserContextHolder.class);

        fetchingService = new FetchingServiceImpl(reservationDAO, statusDAO, locationDAO, equipmentDAO, userContextHolder);


    }

    @Test
    void getEquipmentListIfUserNotLogIn_ShouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fetchingService.getEquipmentList());
    }

    @Test
    void getEquipmentListIfUserIsLogIn_ShouldReturnOptionalWithListInIt() {
        when(equipmentDAO.getEquipmentTable()).thenReturn(equipmentList);
        when(userContextHolder.get()).thenReturn(new UserContext(new User(), "dummy@dummy.com"));

        assertTrue(fetchingService.getEquipmentList().isPresent());
    }

    @Test
    void getLocationListIfUserNotLongIn_ShouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fetchingService.getAvailableLocation());
    }

    @Test
    void getValidLocationListIfUserLogIn_ShouldReturnOptionalWithListOfLocation() {
        when(locationDAO.getLocationTable()).thenReturn(locationList);
        when(userContextHolder.get()).thenReturn(new UserContext(new User(), "dummy@dummy.com"));

        assertTrue(fetchingService.getAvailableLocation().isPresent());
    }

    @Test
    void getValidLocationListAndCheckIfItIsNotEmpty_ShouldReturnNotEmptyList() {
        when(locationDAO.getLocationTable()).thenReturn(locationList);
        when(userContextHolder.get()).thenReturn(new UserContext(new User(), "dummy@dummy.com"));

        assertTrue(fetchingService.getAvailableLocation().isPresent());
        assertFalse(fetchingService.getAvailableLocation().get().isEmpty());
    }

    @Test
    void getStatusListIfUserNotLogIn_ShouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fetchingService.getStatusList());

    }

    @Test
    void getStatusListIfUserIsLogIn_ShouldReturnValidStatusList() {
        when(statusDAO.getStatusTable()).thenReturn(statusList);
        when(userContextHolder.get()).thenReturn(new UserContext(new User(), "dummy@dummy.com"));

        assertTrue(fetchingService.getStatusList().isPresent());
    }

    @Test
    void getValidStatusListAndCheckIfItIsNotEmpty_ShouldNotBeEmpty() {
        when(statusDAO.getStatusTable()).thenReturn(statusList);
        when(userContextHolder.get()).thenReturn(new UserContext(new User(), "dummy@dummy.com"));

        assertTrue(fetchingService.getStatusList().isPresent());
        assertFalse(fetchingService.getStatusList().get().isEmpty());
    }

    @Test
    void getReservationListForNotLogInUser_ShouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fetchingService.getReservationListForSessionUser());

    }

    @Test
    void getReservationListForLogInUser_ShouldReturnNotEmptyOptional() {
        when(userContextHolder.get()).thenReturn(new UserContext(new User(), "dummy@dummy.com"));

        assertTrue(fetchingService.getReservationListForSessionUser().isPresent());
    }

    @Test
    void getValidReservationListWhichIsNotEmpty_ShouldReturnNotEmptyOptionalWithNoEmptyListInside() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        when(userContextHolder.get()).thenReturn(new UserContext(user, "dummy@dummy.com"));
        when(reservationDAO.findReservationsBy(uuid)).thenReturn(reservationRegisterList);

        Optional<List<ReservationRegister>> reservationList = fetchingService.getReservationListForSessionUser();
        assertTrue(reservationList.isPresent());
        assertFalse(reservationList.get().isEmpty());

    }

}
