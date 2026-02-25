package com.yummie.service;

import com.yummie.entity.RefreshTokenEntity;
import com.yummie.entity.UserEntity;
import com.yummie.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refeshTokenRepository;

    public String generateRefreshToken() {
        String token;
        do token = UUID.randomUUID().toString();
        while (refeshTokenRepository.existsByValue(token));
        return token;
    }

    public RefreshTokenEntity createRefreshToken(UserEntity userEntity) {
        String token = generateRefreshToken();
        RefreshTokenEntity refeshTokenEntity = new RefreshTokenEntity();
        refeshTokenEntity.setValue(token);
        refeshTokenEntity.setUserEntity(userEntity);
        refeshTokenRepository.save(refeshTokenEntity);
        return refeshTokenEntity;
    }

    public void renewRefreshToken(RefreshTokenEntity refeshTokenEntity) {
        String refreshToken = generateRefreshToken();
        refeshTokenEntity.setValue(refreshToken);
        refeshTokenRepository.save(refeshTokenEntity);
    }
}
