package com.indeed.cartservice.Service;


import com.indeed.cartservice.DTO.CartDTO;
import com.indeed.cartservice.DTO.CartItemDTO;
import com.indeed.cartservice.Entity.Cart;
import com.indeed.cartservice.Entity.CartItem;
import com.indeed.cartservice.ExternalService.InventoryServiceClient;
import com.indeed.cartservice.Repository.CartRepository;
import com.indeed.inventoryservice.DTO.InventoryItemDTO;
import com.indeed.inventoryservice.Exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private InventoryServiceClient inventoryClient;

    public CartDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));
        return convertToDTO(cart);
    }

    public CartDTO addItem(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        // Get product details from inventory service
        InventoryItemDTO product = inventoryClient.getItem(productId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity of existing item
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item to cart
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(productId);
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        cart.setLastModified(LocalDateTime.now());
        cart = cartRepository.save(cart);

        return convertToDTO(cart);
    }

    public CartDTO updateItemQuantity(Long userId, Long productId, Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        if (quantity == 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        cart.setLastModified(LocalDateTime.now());
        cart = cartRepository.save(cart);

        return convertToDTO(cart);
    }

    public void removeItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        if (!removed) {
            throw new ResourceNotFoundException("Item not found in cart");
        }

        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();
        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
    }

    private Cart createNewCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setLastModified(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setUserId(cart.getUserId());

        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        dto.setTotalAmount(calculateTotal(itemDTOs));

        return dto;
    }

    private CartItemDTO convertToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return dto;
    }

    private BigDecimal calculateTotal(List<CartItemDTO> items) {
        return items.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

