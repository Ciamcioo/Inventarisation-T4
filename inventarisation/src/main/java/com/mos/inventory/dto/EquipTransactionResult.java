package com.mos.inventory.dto;

import java.util.Objects;

public class EquipTransactionResult {
    private EquipTransactionMessage message;


    public EquipTransactionResult(EquipTransactionMessage message) {
        this.message = message;
    }

    public EquipTransactionMessage getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipTransactionResult that = (EquipTransactionResult) o;
        return message == that.message;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message);
    }
}
