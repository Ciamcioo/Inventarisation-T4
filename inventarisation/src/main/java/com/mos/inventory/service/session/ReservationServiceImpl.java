package com.mos.inventory.service.session;

import com.mos.inventory.dto.ReservationMessage;
import com.mos.inventory.dto.ReservationResult;
import com.mos.inventory.dto.UserContext;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.ReservationRegister;
import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.ReservationDAO;
import com.mos.inventory.service.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService{
    public static final int RECRUIT_MAX_RESERVATIONS = 1;
    public static final int MOSOWICZ_MAX_RESERVATIONS = 5;
    public static final int ZARZAD_ADMIN_MAX_RESERVATIONS = 10;

    private final UserContextHolder userContextHolder;
    private final EquipmentDAO equipmentDAO;
    private final ReservationDAO reservationDAO;

    private UserContext userContext;
    private List<Equipment> reservationEquipment;

    public ReservationServiceImpl(EquipmentDAO equipmentDAO,
                                  ReservationDAO reservationDAO,
                                  UserContextHolder userContextHolder) {

        this.equipmentDAO = equipmentDAO;
        this.reservationDAO = reservationDAO;
        this.userContextHolder = userContextHolder;
    }

    @Override
    @Transactional
    public ReservationResult createReservation(Set<UUID> equipmentIDs, Date startDate, Date endDate) {
        Optional<ReservationResult> validationResult;

        validationResult = checkIfUserContextIsPresent();
        if (validationResult.isPresent()) {
            return validationResult.get();
        }

        validationResult = equipmentValidation(equipmentIDs);
        if (validationResult.isPresent()) {
            return validationResult.get();

        }

        validationResult = dateValidation(startDate, endDate);
        if (validationResult.isPresent()) {
            return validationResult.get();

        }

        validationResult = reservationPolicyValidation();
        if (validationResult.isPresent()) {
            return validationResult.get();
        }

        reservationEquipment = equipmentDAO.findAllBy(equipmentIDs);

        reservationEquipment = reservationEquipment.stream()
                                                   .filter(equipment -> equipment.getQuantity() >= 1)
                                                   .toList();

        reservationEquipment.forEach(equipment -> {
                                equipment.setQuantity(equipment.getQuantity() - 1);
                                equipmentDAO.updateEquipment(equipment);
                            });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, 3);
        Date timeout = calendar.getTime();

        ReservationRegister reservationRegister = new ReservationRegister(userContext.getUser(),
                                                                            reservationEquipment,
                                                                            timeout,
                                                                            startDate,
                                                                            endDate);
        reservationDAO.insertReservation(reservationRegister);




        return validationResult.orElseGet(() -> new ReservationResult(ReservationMessage.SUCCESSFUL_RESERVATION));

    }

    private Optional<ReservationResult> reservationPolicyValidation() {
        List<ReservationRegister> reservations = reservationDAO.findReservationsBy(userContext.getUserID());

        int maxReservations = getMaximumNumberOfReservations(userContext.getUserRoleName());
        if (reservations.size() >= maxReservations) {
            return Optional.of(new ReservationResult(ReservationMessage.RESERVATION_ERROR));
        }
        return Optional.empty();
    }

    private int getMaximumNumberOfReservations(String userRoleName) {
        if (userRoleName.equals("Rekrut")) {
           return RECRUIT_MAX_RESERVATIONS;
        }

        if (userRoleName.equals("Mosowicz")) {
            return MOSOWICZ_MAX_RESERVATIONS;
        }

        if (userRoleName.equals("Zarzad") || userRoleName.equals("Admin")) {
            return ZARZAD_ADMIN_MAX_RESERVATIONS;
        }

        return 0;
    }

    private Optional<ReservationResult> equipmentValidation(Set<UUID> equipmentIDs) {
        if(equipmentIDs.isEmpty()) {
            return Optional.of(new ReservationResult(ReservationMessage.EQUIPMENT_ID_LIST_ERROR));
        }
        return Optional.empty();
    }

    private Optional<ReservationResult> checkIfUserContextIsPresent() {
        if (userContext != null) {
            return Optional.empty();
        }

        if (userContextHolder.get() != null) {
            userContext = userContextHolder.get();
            return Optional.empty();
        }

        return Optional.of(new ReservationResult(ReservationMessage.USER_ERROR));

    }

    private Optional<ReservationResult> dateValidation(Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        if (startDate.before(yesterday) || endDate.before(yesterday)) {
            return Optional.of(new ReservationResult((ReservationMessage.DATE_ERROR)));
        }

        if(endDate.before(startDate)) {
            return Optional.of(new ReservationResult((ReservationMessage.DATE_ERROR)));
        }

        return Optional.empty();
    }

    @Override
    public void setUserContextForService(UserContext userContext) {
        this.userContext =userContext;
    }


}
