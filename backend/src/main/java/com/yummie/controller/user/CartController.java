package com.yummie.controller.user;

import com.yummie.model.request.AddToCartRequest;
import com.yummie.model.request.UpdateCartItemRequest;
import com.yummie.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return cartService.getCartItemResponseList(email);
    }

    @PostMapping("/item")
    public ResponseEntity<?> addToCart(Authentication authentication,
                                       @RequestBody AddToCartRequest request) {
        String email = (String) authentication.getPrincipal();
        return cartService.addToCart(request, email);
    }

    @PatchMapping("/item/{id}")
    public ResponseEntity<?> updateCartItem(Authentication authentication,
                                            @PathVariable long id,
                                            @RequestBody UpdateCartItemRequest request) {
        String email = (String) authentication.getPrincipal();
        return cartService.updateCartItem(email, id, request);
    }

    @DeleteMapping("/item")
    public ResponseEntity<?> deleteCartItem(Authentication authentication,
                                            @RequestParam Set<Long> ids) {
        String email = (String) authentication.getPrincipal();
        return cartService.deleteCartItem(email, ids);
    }
}
