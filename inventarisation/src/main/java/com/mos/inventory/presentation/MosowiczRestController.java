package com.mos.inventory.presentation;

import com.mos.inventory.dto.*;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.Location;
import com.mos.inventory.entity.Status;
import com.mos.inventory.service.session.EquipmentService;
import com.mos.inventory.service.session.FetchingService;
import com.mos.inventory.service.session.RentalService;
import com.mos.inventory.service.session.ReservationService;
import org.hibernate.sql.Update;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/mosowicz")
public class MosowiczRestController {
    private final FetchingService fetchingService;
    private final ReservationService reservationService;
    private final RentalService rentalService;
    private final EquipmentService equipmentService;

    public MosowiczRestController(FetchingService fetchingService, ReservationService reservationService, RentalService rentalService, EquipmentService equipmentService) {
        this.fetchingService = fetchingService;
        this.reservationService = reservationService;
        this.rentalService = rentalService;
        this.equipmentService = equipmentService;
    }

    @RequestMapping("/current/rentals")
    public List<RentalContext> getRentals() {
        return fetchingService.getRentalListForSessionUser().orElse(new ArrayList<>());

    }

    @RequestMapping("/current/reservations")
    public List<ReservationContext> getReservations() {
        return fetchingService.getReservationListForSessionUser().orElse(new ArrayList<>());
    }

    @PostMapping("/make/reservation")
    public EquipTransactionResult makeReservation(@RequestBody EquipTransactionRequest request) {
        return reservationService.createReservation(request.getEquipmentIDs(), request.getStartDate(), request.getEndDate(), request.getTransactionType());
    }

    @RequestMapping("/equipment")
    public List<Equipment> getEquipment() {
       return fetchingService.getEquipmentList().orElse(new ArrayList<>());
    }

    @PostMapping("/make/rental")
    public EquipTransactionResult makeRental(@RequestBody EquipTransactionRequest request) {
        return rentalService.createRental(request.getEquipmentIDs(), request.getStartDate(), request.getEndDate(), request.getTransactionType());
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

    @RequestMapping("/technical/status/update")
    public UpdateResult updateEquipmentTechnicalStatus(@RequestParam UUID equipmentID, int technicalStatusId) {
        return equipmentService.updateTechnicalStatusOf(equipmentID, technicalStatusId);

    }

    @DeleteMapping("/make/return")
    public EquipTransactionResult returnRental(@RequestParam UUID rentalID)  {
        return rentalService.returnRental(rentalID);
    }
}
