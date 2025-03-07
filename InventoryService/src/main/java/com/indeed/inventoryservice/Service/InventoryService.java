package com.indeed.inventoryservice.Service;

import com.indeed.inventoryservice.DTO.InventoryItemDTO;
import com.indeed.inventoryservice.Entity.InventoryItem;
import com.indeed.inventoryservice.Exception.ResourceNotFoundException;
import com.indeed.inventoryservice.Repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public List<InventoryItemDTO> getAllItems() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InventoryItemDTO getItemById(Long id) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        return convertToDTO(item);
    }

    public InventoryItemDTO createItem(InventoryItemDTO itemDTO) {
        validateNewItem(itemDTO);
        InventoryItem item = convertToEntity(itemDTO);
        InventoryItem savedItem = inventoryRepository.save(item);
        return convertToDTO(savedItem);
    }

    public InventoryItemDTO updateItem(Long id, InventoryItemDTO itemDTO) {
        InventoryItem existingItem = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        updateEntityFromDTO(existingItem, itemDTO);
        InventoryItem updatedItem = inventoryRepository.save(existingItem);
        return convertToDTO(updatedItem);
    }

    public void deleteItem(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    public boolean updateQuantity(Long id, int quantity) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        if (item.getQuantity() + quantity < 0) {
            return false;
        }

        item.setQuantity(item.getQuantity() + quantity);
        inventoryRepository.save(item);
        return true;
    }

    private void validateNewItem(InventoryItemDTO itemDTO) {
        if (inventoryRepository.existsByName(itemDTO.getName())) {
            throw new IllegalArgumentException("An item with this name already exists");
        }
    }

    private InventoryItemDTO convertToDTO(InventoryItem item) {
        InventoryItemDTO dto = new InventoryItemDTO();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }

    private InventoryItem convertToEntity(InventoryItemDTO dto) {
        InventoryItem entity = new InventoryItem();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    private void updateEntityFromDTO(InventoryItem entity, InventoryItemDTO dto) {
        // Don't update the ID
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setCategory(dto.getCategory());
        entity.setImageUrl(dto.getImageUrl());
    }
}
