package com.yummie.converter;

import com.yummie.entity.UserEntity;
import com.yummie.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public UserEntity toUserEntity(UserEntity userEntity, RegisterRequest request) {
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole("ROLE_USER");
        userEntity.setRegistered(false);
        userEntity.setEnable(false);
        return userEntity;
    }
}
