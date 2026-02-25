package com.yummie.repository;

import com.yummie.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    boolean existsByValue(String value);

    RefreshTokenEntity findByValueAndExpiresAtAfter(String value, LocalDateTime now);
}
