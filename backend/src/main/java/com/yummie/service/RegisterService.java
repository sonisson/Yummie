package com.yummie.service;

import com.yummie.entity.UserEntity;
import com.yummie.model.request.LoginRequest;
import com.yummie.model.request.RegisterRequest;
import com.yummie.model.request.VerificationRequest;
import com.yummie.model.response.LoginResponse;
import com.yummie.repository.UserRepository;
import com.yummie.util.SenderUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final VerificationCodeService verificationCodeService;
    private final UserService userService;
    private final LoginService loginService;
    private final SenderUtil senderUtil;

    public ResponseEntity<?> register(RegisterRequest request) {
        UserEntity userEntity = userRepository.findByEmail(request.getEmail());
        if (userEntity != null && userEntity.isRegistered()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }
        String code = verificationCodeService.generateCode();
        if (userEntity == null) {
            userService.createUser(request, code);
        } else {
            userService.updateUser(userEntity, request, code);
        }
//        senderUtil.sendVerificationCode(request.getEmail(), code);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyAccount(VerificationRequest request,
                                           HttpServletResponse response) {
        UserEntity userEntity = userRepository.findByEmailAndVerificationCodeEntityValueAndVerificationCodeEntityExpiresAtAfter(request.getEmail(), request.getCode(), LocalDateTime.now());
        if (userEntity == null) {
            return ResponseEntity.status(400).body("Verification code is invalid.");
        }
        userService.activeUser(userEntity);
        LoginRequest loginRequest=new LoginRequest(userEntity.getEmail(), request.getPassword());
        return loginService.login(loginRequest,response);
    }
}
