package com.yummie.repository;

import com.yummie.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByIdAndUserEntityEmail(long id, String email);

    @Query("""
            SELECT SUM(oi.quantity * v.price)
            FROM OrderItemEntity oi
            JOIN oi.variantEntity v
            WHERE oi.orderEntity.id = :orderId""")
    long getTotalByOrderId(@Param("orderId") Long orderId);

    Page<OrderEntity> findByLatestOrderStatusEntityStatusEntityTypeAndUserEntityEmail(String statusType, String email, Pageable pageable);
}
