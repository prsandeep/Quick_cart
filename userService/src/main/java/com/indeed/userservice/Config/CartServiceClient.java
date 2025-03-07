package com.indeed.userservice.Config;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.indeed.userservice.DTO.CartDTO;

@FeignClient(name = "CARTSERVICE")
public interface CartServiceClient {
    @GetMapping("/api/cart/{userId}")
    CartDTO getCart(@PathVariable Long userId);

    @PostMapping("/api/cart/{userId}/items")
    CartDTO addItem(@PathVariable Long userId, @RequestParam Long productId, @RequestParam Integer quantity);

    @DeleteMapping("/api/cart/{userId}/items/{productId}")
    void removeItem(@PathVariable Long userId, @PathVariable Long productId);

    @DeleteMapping("/api/cart/{userId}")
    void clearCart(@PathVariable Long userId);
}