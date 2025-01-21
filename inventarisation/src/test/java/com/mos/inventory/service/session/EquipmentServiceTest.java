package com.mos.inventory.service.session;

import com.mos.inventory.dto.EquipTransactionMessage;
import com.mos.inventory.dto.EquipTransactionResult;
import com.mos.inventory.dto.UpdateResult;
import com.mos.inventory.entity.Category;
import com.mos.inventory.entity.Equipment;
import com.mos.inventory.entity.Status;
import com.mos.inventory.entity.TechnicalStatus;
import com.mos.inventory.presistance.EquipmentDAO;
import com.mos.inventory.presistance.TechnicalStatusDTO;
import com.mos.inventory.service.UserContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.mos.inventory.dto.UpdateMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EquipmentServiceTest {
    EquipmentService equipmentService;

    UserContextHolder userContextHolder;
    EquipTxnValidator validator;
    EquipmentDAO equipmentDAO;
    TechnicalStatusDTO technicalStatusDAO;

    Equipment equipment;
    int technicalStatusID;

    @BeforeEach
    void setup() {
        userContextHolder = mock(UserContextHolder.class);
        validator = mock(EquipTxnValidator.class);
        equipmentDAO = mock(EquipmentDAO.class);
        technicalStatusDAO = mock(TechnicalStatusDTO.class);

        equipmentService = new EquipmentServiceImpl(userContextHolder, validator, equipmentDAO, technicalStatusDAO);

        equipment = spy(new Equipment());
        equipment.setId(UUID.randomUUID());
        equipment.setName("ExampleName");
        equipment.setQuantity(10);
        equipment.setDescription("Description");
        equipment.setStatus(new Status(1, "Status"));
        equipment.setCategory(new Category(1, "Category"));
        equipment.setRentals(new ArrayList<>());
        equipment.setReservations(new ArrayList<>());

        technicalStatusID = 1;

        when(validator.checkIfUserContextIsPresent()).thenReturn(Optional.of(new EquipTransactionResult(EquipTransactionMessage.SUCCESSFUL_RESERVATION)));
    }

    @Test
    void updateTechnicalStatusReturnType_ShouldReturnUpdateResultObject() {
        assertInstanceOf(UpdateResult.class, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID));
    }

    @Test
    void updateTechnicalStatusWhenEquipmentIsNull_ShouldReturnUpdateResultWithInvalidEquipmentMessage() {
        assertEquals(EQUIPMENT_NOT_FOUND, equipmentService.updateTechnicalStatusOf(null, technicalStatusID).getMessage());
    }

    @Test
    void updateTechnicalStatusWhenEquipmentUpdate_ShouldReturnUpdateResultWithSuccessfulMessage() {
        when(equipmentDAO.checkIfContainsEquipment(equipment.getId())).thenReturn(true);
        when(technicalStatusDAO.getTechnicalStatusBy(technicalStatusID)).thenReturn(Optional.of(new TechnicalStatus()));
        assertEquals(SUCCESSFUL_UPDATE, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID).getMessage());
    }

    @Test
    void updateTechnicalStatusRetrievingEquipmentFromDAO_ShouldReturnSuccessfulUpdate() {
        when(equipmentDAO.checkIfContainsEquipment(equipment.getId())).thenReturn(true);
        when(technicalStatusDAO.getTechnicalStatusBy(technicalStatusID)).thenReturn(Optional.of(new TechnicalStatus()));
        assertEquals(SUCCESSFUL_UPDATE, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID).getMessage());
        verify(equipmentDAO).checkIfContainsEquipment(equipment.getId());
    }

    @Test
    void updateTechnicalStatusRetrievingEquipmentFromDAO_ShouldReturnEquipmentNotFound() {
        when(equipmentDAO.checkIfContainsEquipment(equipment.getId())).thenReturn(false);
        assertEquals(EQUIPMENT_NOT_FOUND, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID).getMessage());
        verify(equipmentDAO).checkIfContainsEquipment(equipment.getId());
    }

    @Test
    void updateTechnicalStatusRetrievingEquipmentFromDAO_ShouldInvokeFindEquipmentMethodFromDAO() {
        when(equipmentDAO.checkIfContainsEquipment(equipment.getId())).thenReturn(true);
        when(equipmentDAO.findAllBy(Set.of(equipment.getId()))).thenReturn(List.of(equipment));
        when(technicalStatusDAO.getTechnicalStatusBy(technicalStatusID)).thenReturn(Optional.of(new TechnicalStatus()));


        assertEquals(SUCCESSFUL_UPDATE, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID).getMessage());
        verify(equipmentDAO).checkIfContainsEquipment(equipment.getId());
        verify(equipmentDAO).updateEquipment(equipment);
    }

    @Test
    void retrievingTechnicalStatusRecordBasedOnId_ShouldInvokeTechnicalStatusDAO() {
        when(equipmentDAO.checkIfContainsEquipment(equipment.getId())).thenReturn(true);
        when(equipmentDAO.findAllBy(Set.of(equipment.getId()))).thenReturn(List.of(equipment));
        when(technicalStatusDAO.getTechnicalStatusBy(technicalStatusID)).thenReturn(Optional.of(new TechnicalStatus()));

        assertEquals(SUCCESSFUL_UPDATE, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID).getMessage());

        verify(technicalStatusDAO).getTechnicalStatusBy(technicalStatusID);
    }

    @Test
    void retrievingTechnicalStatusForInvalidId_ShouldReturnUpdateResultWithTechnicalStatusNotFoundMessage() {
        when(equipmentDAO.checkIfContainsEquipment(equipment.getId())).thenReturn(true);
        when(equipmentDAO.findAllBy(Set.of(equipment.getId()))).thenReturn(List.of(equipment));
        when(technicalStatusDAO.getTechnicalStatusBy(eq(technicalStatusID))).thenReturn(Optional.empty());

        assertEquals(TECHNICAL_STATUS_NOT_FOUND, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID).getMessage());

        verify(technicalStatusDAO).getTechnicalStatusBy(technicalStatusID);
        verify(equipmentDAO, never()).updateEquipment(equipment);
    }

    @Test
    void settingUpTechnicalStatusWhenValidID_EquipmentShouldInvokeSetUpMethod() {
        when(equipmentDAO.checkIfContainsEquipment(equipment.getId())).thenReturn(true);
        when(equipmentDAO.findAllBy(Set.of(equipment.getId()))).thenReturn(List.of(equipment));
        when(technicalStatusDAO.getTechnicalStatusBy(technicalStatusID)).thenReturn(Optional.of(new TechnicalStatus()));

        assertEquals(SUCCESSFUL_UPDATE, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID).getMessage());

        verify(equipment).setTechnicalStatus(any(TechnicalStatus.class));
        verify(technicalStatusDAO).getTechnicalStatusBy(technicalStatusID);
    }

    @Test
    void validatingIfUserIsLoggedInDuringUpdate_ShouldReturnUserNotLoggedInUpdateResult() {
        when(validator.checkIfUserContextIsPresent()).thenReturn(Optional.empty());

        assertEquals(USER_NOT_LOGGED_IN, equipmentService.updateTechnicalStatusOf(equipment.getId(), technicalStatusID).getMessage());
    }
}
