package com.yummie.service;

import com.yummie.converter.OrderConverter;
import com.yummie.entity.*;
import com.yummie.model.response.*;
import com.yummie.repository.CartItemRepository;
import com.yummie.repository.OrderRepository;
import com.yummie.repository.StatusRepository;
import com.yummie.repository.UserRepository;
import com.yummie.util.PageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderConverter orderConverter;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final StatusRepository statusRepository;
    private final PageUtil pageUtil;

    public ResponseEntity<?> getOrderResponsePage(String statusType, int page, String email) {
        Sort sort = Sort.by("createdAt").descending();
        Page<OrderEntity> orderEntityPage = pageUtil.getPage(page, 10, sort,
                p -> orderRepository.findByLatestOrderStatusEntityStatusEntityTypeAndUserEntityEmail(statusType, email, p));
        return ResponseEntity.ok(orderConverter.toOrderResponsePage(orderEntityPage));
    }

    @Transactional
    public ResponseEntity<?> createOrder(String email, Set<Long> cartItemIds) {
        cartService.validateOwnership(cartItemIds, email);
        List<CartItemEntity> cartItemEntities = cartItemRepository.findByIdIn(cartItemIds);
        UserEntity userEntity = userRepository.findByEmail(email);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserEntity(userEntity);
        List<OrderItemEntity> orderItemEntities = orderConverter.toOrderItemEntityList(cartItemEntities);
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderEntity.addOrderItem(orderItemEntity);
        }
        orderEntity.setOrderItemEntities(orderItemEntities);
        addOrderStatus(orderEntity, "PAYMENT_PENDING", "Đang chờ thanh toán");
        cartItemRepository.deleteAllById(cartItemIds);
        orderEntity = orderRepository.save(orderEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderConverter.toOrderResponse(orderEntity));
    }

    public void addOrderStatus(OrderEntity orderEntity, String statusName, String statusDetail) {
        StatusEntity statusEntity = statusRepository.findByName(statusName);
        if (statusEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found");
        }
        OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
        orderStatusEntity.setStatusEntity(statusEntity);
        orderStatusEntity.setDetail(statusDetail);
        orderEntity.addOrderStatus(orderStatusEntity);
        orderEntity.setLatestOrderStatusEntity(orderStatusEntity);
    }
}
