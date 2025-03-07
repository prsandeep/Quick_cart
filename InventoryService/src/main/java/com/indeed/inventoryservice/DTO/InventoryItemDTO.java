package com.indeed.inventoryservice.DTO;

import com.indeed.inventoryservice.Entity.InventoryItem;

import java.math.BigDecimal;

public class InventoryItemDTO {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventoryItemDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public InventoryItem.ItemCategory getCategory() {
        return category;
    }

    public void setCategory(InventoryItem.ItemCategory category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public InventoryItemDTO(Long id, String name, String description, BigDecimal price, Integer quantity, InventoryItem.ItemCategory category, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private InventoryItem.ItemCategory category;
    private String imageUrl;

    // Getters, setters, constructors
}
