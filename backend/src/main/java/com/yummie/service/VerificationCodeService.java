package com.yummie.service;

import com.yummie.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    public String generateCode() {
        final SecureRandom random = new SecureRandom();
        String code;
        do code = String.format("%06d", random.nextInt(100000));
        while (verificationCodeRepository.existsByValueAndExpiresAtAfter(code, LocalDateTime.now()));
        return code;
    }
}