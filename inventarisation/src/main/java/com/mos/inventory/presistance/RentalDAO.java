package com.mos.inventory.presistance;

import com.mos.inventory.entity.RentalRegister;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface RentalDAO {

    List<RentalRegister> findRentalsFor(UUID userID);
    boolean  checkIfContainsRental(UUID rentalID);
    void removeRental(UUID rentalID);

    @Transactional
    void insertRental(RentalRegister rental);
}
