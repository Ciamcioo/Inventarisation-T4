package com.mos.inventory.presistance;

public class InvalidInsert extends RuntimeException {
    public InvalidInsert(String message) {
        super(message);
    }
}
