package com.mos.inventory.presentation;

import com.mos.inventory.dto.FetchingMessage;
import com.mos.inventory.dto.FetchingResult;
import com.mos.inventory.dto.ReservationRequest;
import com.mos.inventory.dto.ReservationResult;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.Location;
import com.mos.inventory.entity.ReservationRegister;
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


    @PostMapping("/reservation")
    public ReservationResult makeReservation(@RequestBody ReservationRequest request) {

        return reservationService.createReservation(request.getEquipmentIDs(),
                                                    request.getStartDate(),
                                                    request.getEndDate());
    }

    @RequestMapping("/equipment/list")
    public List<Equipment> getEquipmentList() {
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
    public List<ReservationRegister> getReservationForCurrentUser() {
        Optional<List<ReservationRegister>> reservationList = fetchingService.getReservationListForSessionUser();
        return reservationList.orElse(new ArrayList<>());

    }
}
