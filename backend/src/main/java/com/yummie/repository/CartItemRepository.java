package com.yummie.repository;

import com.yummie.entity.CartItemEntity;
import com.yummie.entity.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    CartItemEntity findByVariantEntityIdAndUserEntityEmail(long variantId, String email);

    CartItemEntity findByVariantEntityAndIdNotAndUserEntityEmail(VariantEntity variantEntity, long cartItemId, String email);

    List<CartItemEntity> findByIdIn(Set<Long> cartItemIds);

    List<CartItemEntity> findByUserEntityEmail(String email);

    long countByIdInAndUserEntityEmail(Set<Long> cartItemIdList, String email);

    List<CartItemEntity> findByIdInAndUserEntityEmail(Set<Long> cartItemIds, String email);
}
