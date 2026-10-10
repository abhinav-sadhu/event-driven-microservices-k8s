package com.abhinav.inventory.repository;

import com.abhinav.inventory.entity.InventoryEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEvent, Long> {
}