package com.mos.inventory.dto;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class EquipTransactionRequest {
    private Set<UUID> equipmentIDs;  // Accepts an array of UUIDs in JSON
    private Date startDate;
    private Date endDate;
    private String transactionType;

    public EquipTransactionRequest() {}

    public EquipTransactionRequest(Set<UUID> equipmentIDs, Date startDate, Date endDate, String transactionType) {
        this.equipmentIDs = equipmentIDs;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Set<UUID> getEquipmentIDs() { return equipmentIDs; }
    public void setEquipmentIDs(Set<UUID> equipmentIDs) { this.equipmentIDs = equipmentIDs; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
