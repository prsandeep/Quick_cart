package com.indeed.userservice.Config;

import com.indeed.userservice.DTO.InventoryItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@FeignClient(name = "INVENTORYSERVICE")  // Change from "inventory-service" to "INVENTORYSERVICE"
public interface InventoryServiceClient {
    @GetMapping("/api/inventory")
    List<InventoryItemDTO> getAllItems();

    @GetMapping("/api/inventory/{id}")
    InventoryItemDTO getItem(@PathVariable Long id);
}
