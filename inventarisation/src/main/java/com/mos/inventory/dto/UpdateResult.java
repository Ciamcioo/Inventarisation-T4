package com.mos.inventory.dto;

public class UpdateResult {
    private UpdateMessage message;

    public UpdateResult() { }

    public UpdateResult(UpdateMessage message) {
        this.message = message;
    }

    public UpdateMessage getMessage() {
        return message;
    }
}
