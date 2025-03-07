package com.indeed.inventoryservice.Controller;
import com.indeed.inventoryservice.Service.InventoryService;

import com.indeed.inventoryservice.DTO.InventoryItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getAllItems() {
        return ResponseEntity.ok(inventoryService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<InventoryItemDTO> createItem(@Valid @RequestBody InventoryItemDTO itemDTO) {
        InventoryItemDTO createdItem = inventoryService.createItem(itemDTO);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody InventoryItemDTO itemDTO) {
        return ResponseEntity.ok(inventoryService.updateItem(id, itemDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<Void> updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity) {
        boolean updated = inventoryService.updateQuantity(id, quantity);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}