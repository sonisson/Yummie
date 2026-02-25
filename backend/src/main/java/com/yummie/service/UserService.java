package com.yummie.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.yummie.converter.UserConverter;
import com.yummie.entity.ProfileEntity;
import com.yummie.entity.UserEntity;
import com.yummie.entity.VerificationCodeEntity;
import com.yummie.model.request.RegisterRequest;
import com.yummie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public void createUserFromGoogle(GoogleIdToken.Payload payload) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(payload.getEmail());
        userEntity.setRole("ROLE_USER");
        userEntity.setRegistered(true);
        userEntity.setEnable(true);
        userEntity.setAuthProvider("google");
        userEntity.addProfile(new ProfileEntity());
        userRepository.save(userEntity);
    }

    public void createUser(RegisterRequest request, String code) {
        UserEntity userEntity = userConverter.toUserEntity(new UserEntity(), request);
        VerificationCodeEntity codeEntity = new VerificationCodeEntity();
        codeEntity.setValue(code);
        codeEntity.setUserEntity(userEntity);
        userEntity.setVerificationCodeEntity(codeEntity);
        userEntity.addProfile(new ProfileEntity());
        userRepository.save(userEntity);
    }

    public void updateUser(UserEntity userEntity, RegisterRequest request, String code) {
        userEntity = userConverter.toUserEntity(userEntity, request);
        userEntity.getVerificationCodeEntity().setValue(code);
        userRepository.save(userEntity);
    }

    public void activeUser(UserEntity userEntity) {
        userEntity.setRegistered(true);
        userEntity.setEnable(true);
        userRepository.save(userEntity);
    }
}
