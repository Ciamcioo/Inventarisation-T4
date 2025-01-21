package com.mos.inventory.service.session;

import com.mos.inventory.dto.RentalContext;
import com.mos.inventory.dto.ReservationContext;
import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.*;
import com.mos.inventory.presistance.*;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FetchingServiceTest {
    FetchingService fetchingService;
    RentalDAO rentalDAO;
    ReservationDAO reservationDAO;
    StatusDAO statusDAO;
    LocationDAO locationDAO;
    EquipmentDAO equipmentDAO;
    UserContextHolder userContextHolder;

    List<Equipment> equipmentList;
    List<Location> locationList;
    List<Status> statusList;
    List<ReservationRegister> reservationRegisterList;
    List<RentalRegister> rentalRegisterList;
    @BeforeEach
    void setup() {
        rentalRegisterList = new ArrayList<>();

        rentalRegisterList.add(new RentalRegister());
        rentalRegisterList.add(new RentalRegister());
        rentalRegisterList.add(new RentalRegister());

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

        rentalDAO = mock(RentalDAO.class);
        reservationDAO = mock(ReservationDAO.class);
        statusDAO = mock(StatusDAO.class);
        locationDAO = mock(LocationDAO.class);
        equipmentDAO = mock(EquipmentDAO.class);
        userContextHolder = mock(UserContextHolder.class);

        fetchingService = new FetchingServiceImpl(rentalDAO,
                                                  reservationDAO,
                                                  statusDAO,
                                                  locationDAO,
                                                  equipmentDAO,
                                                  userContextHolder);


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

        Optional<List<ReservationContext>> reservationList = fetchingService.getReservationListForSessionUser();
        assertTrue(reservationList.isPresent());
        assertFalse(reservationList.get().isEmpty());
    }

    @Test
    void getRentalContextListForUserWhoIsLoggedIn_ShouldReturnNotEmptyOptional() {
        when(userContextHolder.get()).thenReturn(new UserContext(new User(), "dummy@dummy.com"));

        assertTrue(fetchingService.getRentalListForSessionUser().isPresent());
    }

    @Test
    void getRentalContextListForUserWhoIsLoggedIn_ListShouldNotBeEmpty() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);

        when(userContextHolder.get()).thenReturn(new UserContext(user, "dummy@dummy@gmail.com"));
        when(rentalDAO.findRentalsFor(uuid)).thenReturn(rentalRegisterList);

        assertTrue(fetchingService.getRentalListForSessionUser().isPresent());
        assertEquals(3, fetchingService.getRentalListForSessionUser().get().size());
    }

    @Test
    void getRentalContextListForUserWhoIsNotLoggedIn_OptionalShouldBeEmpty() {
        assertFalse(fetchingService.getRentalListForSessionUser().isPresent());
    }

    @Test
    void getRentalContextListForUserWhoHasTwoRentals_OptionalShouldReturnRentalListWithWTwoElements() {
        rentalRegisterList.remove(0);
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);

        when(userContextHolder.get()).thenReturn(new UserContext(user, "dummy@dummy@gmail.com"));
        when(rentalDAO.findRentalsFor(user.getId())).thenReturn(rentalRegisterList);

        assertEquals(2, fetchingService.getRentalListForSessionUser().get().size());

        verify(rentalDAO).findRentalsFor(uuid);
    }

}
