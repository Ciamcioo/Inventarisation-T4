package com.mos.inventory.service.session;

import com.mos.inventory.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FetchingService {

    Optional<List<Equipment>> getEquipmentList();

    Optional<List<Location>> getAvailableLocation();

    Optional<List<Status>> getStatusList();


    Optional<List<ReservationRegister>> getReservationListForSessionUser();
}
