package com.yummie.repository;

import com.yummie.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    UserEntity findByEmailAndVerificationCodeEntityValueAndVerificationCodeEntityExpiresAtAfter(String email, String code, LocalDateTime now);
}