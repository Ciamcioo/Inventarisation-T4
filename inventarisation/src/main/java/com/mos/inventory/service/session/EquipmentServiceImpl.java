package com.mos.inventory.service.session;

import com.mos.inventory.dto.UpdateMessage;
import com.mos.inventory.dto.UpdateResult;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.TechnicalStatus;
import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.TechnicalStatusDTO;
import com.mos.inventory.service.UserContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    private final UserContextHolder userContextHolder;
    private final EquipTxnValidator validator;
    private final EquipmentDAO equipmentDAO;
    private final TechnicalStatusDTO technicalStatusDTO;

    public EquipmentServiceImpl(UserContextHolder userContextHolder, EquipTxnValidator validator, EquipmentDAO equipmentDAO, TechnicalStatusDTO technicalStatusDTO) {
        this.userContextHolder = userContextHolder;
        this.validator = validator;
        this.equipmentDAO = equipmentDAO;
        this.technicalStatusDTO = technicalStatusDTO;
    }

    @Override
    public UpdateResult updateTechnicalStatusOf(UUID id, int technicalStatusID) {
        if (validator.checkIfUserContextIsPresent().isEmpty()) {
            return new UpdateResult(UpdateMessage.USER_NOT_LOGGED_IN);
        }

        if (!equipmentDAO.checkIfContainsEquipment(id)) {
            return new UpdateResult(UpdateMessage.EQUIPMENT_NOT_FOUND);
        }

        List<Equipment> equipmentToUpdate = equipmentDAO.findAllBy(Set.of(id));
        Optional<TechnicalStatus> technicalStatus =  technicalStatusDTO.getTechnicalStatusBy(technicalStatusID);
        if (technicalStatus.isEmpty()) {
            return new UpdateResult(UpdateMessage.TECHNICAL_STATUS_NOT_FOUND);
        }

        equipmentToUpdate.forEach(equipment -> equipment.setTechnicalStatus(technicalStatus.get()));
        equipmentToUpdate.forEach(equipmentDAO::updateEquipment);

        return  new UpdateResult(UpdateMessage.SUCCESSFUL_UPDATE);
    }
}
