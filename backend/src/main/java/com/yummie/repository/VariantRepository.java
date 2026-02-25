package com.yummie.repository;

import com.yummie.entity.ProductEntity;
import com.yummie.entity.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<VariantEntity, Long> {

    List<VariantEntity> findByProductEntity(ProductEntity productEntity);
}
