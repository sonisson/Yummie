package com.yummie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`")
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItemEntities = new ArrayList<>();

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<OrderStatusEntity> orderStatusEntities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "latest_order_status_id")
    private OrderStatusEntity latestOrderStatusEntity;

    public void addOrderItem(OrderItemEntity orderItemEntity) {
        orderItemEntity.setOrderEntity(this);
        this.orderItemEntities.add(orderItemEntity);
    }

    public void addOrderStatus(OrderStatusEntity orderStatusEntity) {
        orderStatusEntity.setOrderEntity(this);
        this.orderStatusEntities.add(orderStatusEntity);
    }

    public long getTotal() {
        return orderItemEntities
                .stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

}
