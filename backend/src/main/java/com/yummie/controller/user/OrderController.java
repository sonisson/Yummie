package com.yummie.controller.user;

import com.yummie.service.CartService;
import com.yummie.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/preview")
    public ResponseEntity<?> getPreview(Authentication authentication,
                                        @RequestParam Set<Long> ids) {
        String email = (String) authentication.getPrincipal();
        return cartService.getCartItemResponseList(email, ids);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(Authentication authentication,
                                         @RequestBody Set<Long> cartItemIds) {
        String email = (String) authentication.getPrincipal();
        return orderService.createOrder(email, cartItemIds);
    }

    @GetMapping
    public ResponseEntity<?> getOrderList(Authentication authentication,
                                          @RequestParam String status,
                                          @RequestParam int page) {
        String email = (String) authentication.getPrincipal();
        return orderService.getOrderResponsePage(status, page, email);
    }
}
