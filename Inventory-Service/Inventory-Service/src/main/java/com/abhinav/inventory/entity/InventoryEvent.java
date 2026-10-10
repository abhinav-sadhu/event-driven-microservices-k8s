package com.abhinav.inventory.entity; // पैकेज का नाम अपने अनुसार चेक कर लें

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_events")
public class InventoryEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}