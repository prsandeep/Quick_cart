package com.indeed.cartservice.DTO;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {
    private Long userId;
    private List<CartItemDTO> items;

    public CartDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public CartDTO(Long userId, List<CartItemDTO> items, BigDecimal totalAmount) {
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    private BigDecimal totalAmount;

    // Getters, setters
}
