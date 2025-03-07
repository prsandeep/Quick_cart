package com.indeed.shippingservice.Service;

import com.indeed.inventoryservice.Exception.ResourceNotFoundException;
import com.indeed.shippingservice.DTO.OrderDTO;
import com.indeed.shippingservice.DTO.OrderItemDTO;
import com.indeed.shippingservice.DTO.ShippingAddressDTO;
import com.indeed.shippingservice.Entity.Order;
import com.indeed.shippingservice.Entity.OrderItem;
import com.indeed.shippingservice.Entity.ShippingAddress;
import com.indeed.shippingservice.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShippingService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderDTO createOrder(Long userId, ShippingAddressDTO shippingAddress, List<OrderItemDTO> items) {
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(convertToEntity(shippingAddress));

        // Ensure order items list is initialized
        order.setItems(new ArrayList<>());

        // Add items to order
        for (OrderItemDTO itemDTO : items) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(itemDTO.getProductId());
            item.setProductName(itemDTO.getProductName());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            order.getItems().add(item);
        }

        // Generate tracking number
        order.setTrackingNumber(generateTrackingNumber());

        // Calculate estimated delivery date
        order.setEstimatedDeliveryDate(calculateEstimatedDeliveryDate());

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return convertToDTO(order);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(newStatus);

        if (newStatus == Order.OrderStatus.SHIPPED) {
            order.setShippedDate(LocalDateTime.now());
        }

        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    private String generateTrackingNumber() {
        return "QC" + System.currentTimeMillis() +
                RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }

    private LocalDateTime calculateEstimatedDeliveryDate() {
        return LocalDateTime.now().plusDays(5); // Simplified calculation
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(order, dto);
        dto.setStatus(order.getStatus().name());

        // Convert Shipping Address
        if (order.getShippingAddress() != null) {
            ShippingAddressDTO shippingDTO = new ShippingAddressDTO();
            BeanUtils.copyProperties(order.getShippingAddress(), shippingDTO);
            dto.setShippingAddress(shippingDTO);
        }

        // Convert Items
        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        dto.setTotalAmount(calculateTotal(order.getItems()));
        return dto;
    }

    private OrderItemDTO convertToDTO(OrderItem item) {
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(item.getProductId());
        itemDTO.setProductName(item.getProductName());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setPrice(item.getPrice());
        return itemDTO;
    }

    private ShippingAddress convertToEntity(ShippingAddressDTO dto) {
        ShippingAddress entity = new ShippingAddress();
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setZipCode(dto.getZipCode());
        entity.setCountry(dto.getCountry());
        return entity;
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
