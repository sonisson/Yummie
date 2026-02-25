package com.yummie.converter;

import com.yummie.entity.CartItemEntity;
import com.yummie.model.response.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartConverter {

    private final ModelMapper modelMapper;
    private final AttributeConverter attributeConverter;

    public CartItemResponse toCartItemResponse(CartItemEntity cartItemEntity) {
        CartItemResponse cartItemResponse = modelMapper.map(cartItemEntity, CartItemResponse.class);
        cartItemResponse.setPrice(cartItemEntity.getVariantEntity().getPrice());
        cartItemResponse.setProductName(cartItemEntity.getVariantEntity().getProductEntity().getName());
        cartItemResponse.setProductId(cartItemEntity.getVariantEntity().getProductEntity().getId());
        cartItemResponse.setProductImageUrl(cartItemEntity.getVariantEntity().getProductEntity().getImageUrl());
        cartItemResponse.setProductAttributes(attributeConverter.toProductAttributeMap(
                cartItemEntity.getVariantEntity().getProductEntity().getVariantEntities()));
        cartItemResponse.setVariantAttributes(attributeConverter.toVariantAttributeMap(
                cartItemEntity.getVariantEntity().getAttributeEntities()));
        return cartItemResponse;
    }

    public List<CartItemResponse> toCartItemResponseList(List<CartItemEntity> cartItemEntities) {
        return cartItemEntities.stream().map(this::toCartItemResponse).toList();
    }
}
