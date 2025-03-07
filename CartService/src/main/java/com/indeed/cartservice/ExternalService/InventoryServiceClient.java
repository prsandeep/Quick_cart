package com.indeed.cartservice.ExternalService;

import com.indeed.inventoryservice.DTO.InventoryItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "INVENTORYSERVICE")
public interface InventoryServiceClient {
    @GetMapping("/api/inventory/{id}")
    InventoryItemDTO getItem(@PathVariable Long id);

    @PutMapping("/api/inventory/{id}/quantity")
    ResponseEntity<Void> updateQuantity(@PathVariable Long id, @RequestParam int quantity);
}