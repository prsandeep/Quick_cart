package com.indeed.userservice.Config;
import com.indeed.shippingservice.DTO.CreateOrderRequest;
import com.indeed.userservice.DTO.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "SHIPPINGSERVICE")
public interface ShippingServiceClient {
    @PostMapping("/api/shipping/orders")
    OrderDTO createOrder(@RequestParam Long userId, @RequestBody CreateOrderRequest request);

    @GetMapping("/api/shipping/orders/user/{userId}")
    List<OrderDTO> getUserOrders(@PathVariable Long userId);
}