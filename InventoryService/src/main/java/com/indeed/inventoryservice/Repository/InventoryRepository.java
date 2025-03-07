package com.indeed.inventoryservice.Repository;

import com.indeed.inventoryservice.Entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByCategory(InventoryItem.ItemCategory category);
    List<InventoryItem> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}
