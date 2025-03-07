package com.indeed.shippingservice.Controller;

import com.indeed.shippingservice.DTO.CreateOrderRequest;
import com.indeed.shippingservice.DTO.OrderDTO;
import com.indeed.shippingservice.Entity.Order;
import com.indeed.shippingservice.Service.ShippingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestParam Long userId,
            @Valid @RequestBody CreateOrderRequest request) {
        OrderDTO order = shippingService.createOrder(
                userId,
                request.getShippingAddress(),
                request.getItems()
        );
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(shippingService.getOrder(orderId));
    }

    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(shippingService.getUserOrders(userId));
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(shippingService.updateOrderStatus(orderId, status));
    }
}