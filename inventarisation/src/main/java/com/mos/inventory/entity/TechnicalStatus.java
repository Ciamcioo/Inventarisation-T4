package com.mos.inventory.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "technical_status")
public class TechnicalStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "description", nullable = false)
    private String description;

    public TechnicalStatus() { }

    public TechnicalStatus(int id) {
        this.id = id;
    }

    public TechnicalStatus(String description, int id) {
        this.description = description;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
