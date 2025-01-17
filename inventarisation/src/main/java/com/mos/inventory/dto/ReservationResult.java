package com.mos.inventory.dto;

import java.util.Objects;

public class ReservationResult {
    private ReservationMessage message;


    public ReservationResult(ReservationMessage message) {
        this.message = message;
    }

    public ReservationMessage getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationResult that = (ReservationResult) o;
        return message == that.message;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message);
    }
}
