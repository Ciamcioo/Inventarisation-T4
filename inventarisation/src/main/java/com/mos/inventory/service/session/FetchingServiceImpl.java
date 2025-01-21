package com.mos.inventory.service.session;

import com.mos.inventory.dto.RentalContext;
import com.mos.inventory.dto.ReservationContext;
import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.*;
import com.mos.inventory.presistance.*;
import com.mos.inventory.service.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FetchingServiceImpl implements FetchingService {
    private final RentalDAO rentalDAO;
    private final ReservationDAO reservationDAO;
    private final StatusDAO statusDAO;
    private final LocationDAO locationDAO;
    private final EquipmentDAO equipmentDAO;
    private final UserContextHolder userContextHolder;

    private UserContext userContext;

    @Autowired
    public FetchingServiceImpl(RentalDAO rentalDAO, ReservationDAO reservationDAO, StatusDAO statusDAO, LocationDAO locationDAO, EquipmentDAO equipmentDAO, UserContextHolder userContextHolder) {
        this.rentalDAO = rentalDAO;
        this.reservationDAO = reservationDAO;
        this.statusDAO = statusDAO;
        this.locationDAO = locationDAO;
        this.equipmentDAO = equipmentDAO;
        this.userContextHolder = userContextHolder;
    }


    @Override
    public Optional<List<Equipment>> getEquipmentList() {
        if (userContext == null) {
            userContext = userContextHolder.get();
        }

        if (userContext == null) {
            return Optional.empty();
        }

        return Optional.of(equipmentDAO.getEquipmentTable());
    }

    @Override
    public Optional<List<Location>> getAvailableLocation() {
        if (userContext == null) {
            userContext = userContextHolder.get();
        }

        if (userContext == null) {
            return Optional.empty();
        }

        return Optional.of(locationDAO.getLocationTable());
    }

    @Override
    public Optional<List<Status>> getStatusList() {
        if (userContext == null) {
            userContext = userContextHolder.get();
        }

        if (userContext == null) {
            return Optional.empty();
        }

        return Optional.of(statusDAO.getStatusTable());
    }

    @Override
    public Optional<List<ReservationContext>> getReservationListForSessionUser() {
        if (userContext == null) {
            userContext = userContextHolder.get();
        }

        if (userContext == null) {
            return Optional.empty();
        }

        List<ReservationRegister> rawResrvationList = reservationDAO.findReservationsBy(userContext.getUserID());
        List<ReservationContext> packedReservatinoList = new ArrayList<>();

        rawResrvationList.forEach(rawReservation -> {
            packedReservatinoList.add(new ReservationContext(rawReservation));
        });

        return Optional.of(packedReservatinoList);
    }

    @Override
    public Optional<List<RentalContext>> getRentalListForSessionUser() {
        if (userContext == null) {
            userContext = userContextHolder.get();
        }

        if (userContext == null) {
            return Optional.empty();
        }

        List<RentalRegister> rawRentalList = rentalDAO.findRentalsFor(userContext.getUserID());
        List<RentalContext> rentalContexts = new ArrayList<>();
        rawRentalList.forEach(rawRental ->
            {
                rentalContexts.add(new RentalContext(rawRental));

            });

        return Optional.of(rentalContexts);
    }

}
