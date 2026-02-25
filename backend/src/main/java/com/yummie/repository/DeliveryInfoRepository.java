package com.yummie.repository;

import com.yummie.entity.DeliveryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfoEntity, Long> {

    public List<DeliveryInfoEntity> findByUserEntityEmailOrderByIsDefaultDesc(String email);

    public DeliveryInfoEntity findByUserEntityEmailAndIsDefaultTrue(String email);
}
