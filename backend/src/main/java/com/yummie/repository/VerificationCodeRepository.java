package com.yummie.repository;

import com.yummie.entity.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {

    boolean existsByValueAndExpiresAtAfter(String code, LocalDateTime now);
}