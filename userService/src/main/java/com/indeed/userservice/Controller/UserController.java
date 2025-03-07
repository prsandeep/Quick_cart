package com.indeed.userservice.Controller;

import com.indeed.shippingservice.DTO.CreateOrderRequest;
import com.indeed.shippingservice.DTO.OrderItemDTO;
import com.indeed.shippingservice.DTO.ShippingAddressDTO;
import com.indeed.userservice.DTO.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.indeed.userservice.Entity.User;
import com.indeed.userservice.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.indeed.userservice.Config.InventoryServiceClient;
import com.indeed.userservice.Config.CartServiceClient;
import com.indeed.userservice.Config.ShippingServiceClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private InventoryServiceClient inventoryClient;

    @Autowired
    private CartServiceClient cartClient;

    @Autowired
    private ShippingServiceClient shippingClient;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        System.out.print("user register successfully");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRegistrationDTO user,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }
        userService.registerUser(user);
        System.out.print("redirect to login page");
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public String dashboard(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        // Fetch inventory items
        List<InventoryItemDTO> items = inventoryClient.getAllItems();

        // Fetch user's cart
        CartDTO cart = cartClient.getCart(user.getId());

        // Fetch user's orders
        List<OrderDTO> orders = shippingClient.getUserOrders(user.getId());
        System.out.print("welcome to deshboard");
        model.addAttribute("items", items);
        model.addAttribute("cart", cart);
        model.addAttribute("orders", orders);

        return "dashboard";
    }

    @PostMapping("/cart/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> addToCart(Authentication authentication,
                                             @RequestParam Long productId,
                                             @RequestParam Integer quantity) {
        User user = userService.findByUsername(authentication.getName());
        System.out.print("Item add successfully");
        return ResponseEntity.ok(cartClient.addItem(user.getId(), productId, quantity));
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public String checkout(Authentication authentication,
                           @ModelAttribute ShippingAddressDTO shippingAddress) {
        User user = userService.findByUsername(authentication.getName());
        CartDTO cart = cartClient.getCart(user.getId());

        // Create order request
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setShippingAddress(shippingAddress);
        orderRequest.setItems(convertCartItemsToOrderItems(cart.getItems()));

        // Create order
        shippingClient.createOrder(user.getId(), orderRequest);

        // Clear cart
        cartClient.clearCart(user.getId());

        return "redirect:/orders";
    }
    private List<OrderItemDTO> convertCartItemsToOrderItems(List<CartItemDTO> cartItems) {
        return cartItems.stream().map(cartItem -> {
            OrderItemDTO orderItem = new OrderItemDTO();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            return orderItem;
        }).collect(Collectors.toList());
    }
    @GetMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public String orders(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + authentication.getName());
        }

        List<OrderDTO> orders = new ArrayList<>();
        try {
            log.info("Fetching orders for user ID: {}", user.getId());
            orders = shippingClient.getUserOrders(user.getId());
            log.info("Fetched {} orders", orders.size());
        } catch (Exception e) {
            log.error("Failed to fetch orders for user {}", user.getId(), e);
        }


        model.addAttribute("orders", orders);
        return "orders";
    }


//    @GetMapping("/orders")
//    @PreAuthorize("isAuthenticated()")
//    public String orders(Model model, Authentication authentication) {
//        User user = userService.findByUsername(authentication.getName());
//        List<OrderDTO> orders = shippingClient.getUserOrders(user.getId());
//        model.addAttribute("orders", orders);
//        return "orders";
//    }

}