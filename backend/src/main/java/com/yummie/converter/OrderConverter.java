package com.yummie.converter;

import com.yummie.entity.*;
import com.yummie.model.response.OrderItemResponse;
import com.yummie.model.response.OrderResponse;
import com.yummie.model.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter {

    private final ModelMapper modelMapper;
    private final AttributeConverter attributeConverter;

    public OrderItemEntity toOrderItemEntity(CartItemEntity cartItemEntity) {
        OrderItemEntity orderItemEntity = modelMapper.map(cartItemEntity, OrderItemEntity.class);
        orderItemEntity.setPrice(cartItemEntity.getVariantEntity().getPrice());
        return orderItemEntity;
    }

    public List<OrderItemEntity> toOrderItemEntityList(List<CartItemEntity> cartItemEntities) {
        return cartItemEntities.stream().map(this::toOrderItemEntity).toList();
    }

    public OrderItemResponse toOrderItemResponse(OrderItemEntity orderItemEntity) {
        OrderItemResponse orderItemResponse = modelMapper.map(orderItemEntity, OrderItemResponse.class);
        orderItemResponse.setProductName(orderItemEntity.getVariantEntity().getProductEntity().getName());
        orderItemResponse.setProductImageUrl(orderItemEntity.getVariantEntity().getProductEntity().getImageUrl());
        orderItemResponse.setAttributes(attributeConverter.toVariantAttributeMap(orderItemEntity.getVariantEntity().getAttributeEntities()));
        return orderItemResponse;
    }

    public List<OrderItemResponse> toOrderItemResponseList(List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream().map(this::toOrderItemResponse).toList();
    }

    public OrderResponse toOrderResponse(OrderEntity orderEntity) {
        OrderResponse orderResponse = modelMapper.map(orderEntity, OrderResponse.class);
        orderResponse.setItems(toOrderItemResponseList(orderEntity.getOrderItemEntities()));
        return orderResponse;
    }

    public List<OrderResponse> toOrderResponseList(List<OrderEntity> orderEntities) {
        return orderEntities.stream().map(this::toOrderResponse).toList();
    }

    public PageResponse<OrderResponse> toOrderResponsePage(Page<OrderEntity> orderEntityPage) {
        return new PageResponse<>(toOrderResponseList(orderEntityPage.getContent()),
                orderEntityPage.getTotalPages(),
                orderEntityPage.getNumber());
    }
}
