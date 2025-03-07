package com.indeed.shippingservice.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public ShippingAddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDateTime shippedDate) {
        this.shippedDate = shippedDate;
    }

    public LocalDateTime getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderDTO() {
    }

    public OrderDTO(Long id, Long userId, List<OrderItemDTO> items, ShippingAddressDTO shippingAddress, String status, String trackingNumber, LocalDateTime orderDate, LocalDateTime shippedDate, LocalDateTime estimatedDeliveryDate, BigDecimal totalAmount) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.trackingNumber = trackingNumber;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.totalAmount = totalAmount;
    }

    private List<OrderItemDTO> items;
    private ShippingAddressDTO shippingAddress;
    private String status;
    private String trackingNumber;
    private LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private LocalDateTime estimatedDeliveryDate;
    private BigDecimal totalAmount;

    // Getters, setters
}
