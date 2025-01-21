package com.mos.inventory.presentation;

import com.mos.inventory.dto.*;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.Location;
import com.mos.inventory.entity.Status;
import com.mos.inventory.service.session.FetchingService;
import com.mos.inventory.service.session.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/recruit", produces = "application/json")
public class RecruitRestController {
    private final ReservationService reservationService;
    private final FetchingService fetchingService;

    @Autowired
    public RecruitRestController(ReservationService reservationService, FetchingService fetchingService) {
        this.reservationService = reservationService;
        this.fetchingService = fetchingService;
    }


    @PostMapping("/make/reservation")
    public EquipTransactionResult makeReservation(@RequestBody EquipTransactionRequest request) {

        return reservationService.createReservation(request.getEquipmentIDs(),
                                                    request.getStartDate(),
                                                    request.getEndDate(),
                                                    request.getTransactionType());
    }

    @RequestMapping("/equipment/list")
    public List<Equipment> getEquipment() {
        Optional<List<Equipment>> equipmentList = fetchingService.getEquipmentList();
        return equipmentList.orElse(new ArrayList<>());
    }

    @RequestMapping("/locations")
    public List<Location> getAvailableLocations() {
        Optional<List<Location>> locationList = fetchingService.getAvailableLocation();
        return locationList.orElse(new ArrayList<>());
    }

    @RequestMapping("/status")
    public List<Status> getAvailableStatus() {
        Optional<List<Status>> statusList = fetchingService.getStatusList();
        return statusList.orElse(new ArrayList<>());
    }

    @RequestMapping("/current/reservations")
    public List<ReservationContext> getReservationForCurrentUser() {
        Optional<List<ReservationContext>> reservationList = fetchingService.getReservationListForSessionUser();
        return reservationList.orElse(new ArrayList<>());

    }
}
