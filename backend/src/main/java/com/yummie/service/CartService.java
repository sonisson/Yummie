package com.yummie.service;

import com.yummie.converter.CartConverter;
import com.yummie.entity.CartItemEntity;
import com.yummie.entity.UserEntity;
import com.yummie.entity.VariantEntity;
import com.yummie.model.request.AddToCartRequest;
import com.yummie.model.request.UpdateCartItemRequest;
import com.yummie.repository.CartItemRepository;
import com.yummie.repository.UserRepository;
import com.yummie.repository.VariantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartConverter cartConverter;
    private final VariantService variantService;
    private final VariantRepository variantRepository;

    public void validateOwnership(Set<Long> cartItemIds, String email) {
        if (cartItemIds.size() != cartItemRepository.countByIdInAndUserEntityEmail(cartItemIds, email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this cart item.");
        }
    }

    public ResponseEntity<?> getCartItemResponseList(String email) {
        List<CartItemEntity> cartItemEntities = cartItemRepository.findByUserEntityEmail(email);
        return ResponseEntity.ok(cartConverter.toCartItemResponseList(cartItemEntities));
    }

    public ResponseEntity<?> getCartItemResponseList(String email, Set<Long> cartItemIds) {
        validateOwnership(cartItemIds, email);
        List<CartItemEntity> cartItemEntities = cartItemRepository.findByIdInAndUserEntityEmail(cartItemIds, email);
        return ResponseEntity.ok(cartConverter.toCartItemResponseList(cartItemEntities));
    }

    public ResponseEntity<?> addToCart(AddToCartRequest request, String email) {
        long variantId = request.getVariantId();
        int quantity = request.getQuantity();
        CartItemEntity cartItemEntity = cartItemRepository.findByVariantEntityIdAndUserEntityEmail(variantId, email);
        if (cartItemEntity == null) {
            cartItemEntity = new CartItemEntity();
            VariantEntity variantEntity = variantRepository.findById(variantId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Variant not found."));
            UserEntity userEntity = userRepository.findByEmail(email);
            cartItemEntity.setVariantEntity(variantEntity);
            cartItemEntity.setUserEntity(userEntity);
            cartItemEntity.setQuantity(quantity);
        } else {
            cartItemEntity.setQuantity(cartItemEntity.getQuantity() + quantity);
        }
        cartItemRepository.save(cartItemEntity);
        return ResponseEntity.noContent().build();
    }

    public void updateQuantity(CartItemEntity cartItemEntity, Integer quantity) {
        if (quantity != null && quantity > 0) cartItemEntity.setQuantity(quantity);
    }

    public void updateVariant(CartItemEntity cartItemEntity, Map<String, String> attributes, String email) {
        if (attributes == null) return;
        VariantEntity variantEntity = variantService.getVariantEntity(
                cartItemEntity.getVariantEntity().getProductEntity(),
                attributes
        );
        CartItemEntity existingCartItemEntity = cartItemRepository
                .findByVariantEntityAndIdNotAndUserEntityEmail(variantEntity, cartItemEntity.getId(), email);
        if (existingCartItemEntity != null) {
            cartItemEntity.setQuantity(cartItemEntity.getQuantity() + existingCartItemEntity.getQuantity());
            cartItemRepository.delete(existingCartItemEntity);
        }
        cartItemEntity.setVariantEntity(variantEntity);
    }

    @Transactional
    public ResponseEntity<?> updateCartItem(String email, long id, UpdateCartItemRequest request) {
        CartItemEntity cartItemEntity = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));
        if (!cartItemEntity.getUserEntity().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this cart item");
        }
        updateQuantity(cartItemEntity, request.getQuantity());
        updateVariant(cartItemEntity, request.getAttributes(), email);
        cartItemRepository.save(cartItemEntity);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> deleteCartItem(String email, Set<Long> cartItemIds) {
        validateOwnership(cartItemIds, email);
        cartItemRepository.deleteAllById(cartItemIds);
        return ResponseEntity.noContent().build();
    }
}
