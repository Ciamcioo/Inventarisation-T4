package com.mos.inventory.service.session;

import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.Location;
import com.mos.inventory.entity.ReservationRegister;
import com.mos.inventory.entity.Status;
import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.LocationDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.presistance.StatusDAO;
import com.mos.inventory.service.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FetchingServiceImpl implements FetchingService {
    private final ReservationDAO reservationDAO;
    private final StatusDAO statusDAO;
    private final LocationDAO locationDAO;
    private final EquipmentDAO equipmentDAO;
    private final UserContextHolder userContextHolder;

    private UserContext userContext;

    @Autowired
    public FetchingServiceImpl(ReservationDAO reservationDAO, StatusDAO statusDAO, LocationDAO locationDAO, EquipmentDAO equipmentDAO, UserContextHolder userContextHolder) {
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
    public Optional<List<ReservationRegister>> getReservationListForSessionUser() {
        if (userContext == null) {
            userContext = userContextHolder.get();
        }

        if (userContext == null) {
            return Optional.empty();
        }

        return Optional.of(reservationDAO.findReservationsBy(userContext.getUserID()));
    }
}
