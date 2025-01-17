package com.mos.inventory.presistance;

import com.mos.inventory.entity.ReservationRegister;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationDAO {
    List<ReservationRegister> findReservationsBy(UUID userID);

    @Transactional
    void insertReservation(ReservationRegister newReservations);
}
