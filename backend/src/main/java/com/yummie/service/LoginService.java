package com.yummie.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.yummie.entity.RefreshTokenEntity;
import com.yummie.entity.UserEntity;
import com.yummie.model.request.GoogleLoginRequest;
import com.yummie.model.request.LoginRequest;
import com.yummie.model.response.LoginResponse;
import com.yummie.repository.UserRepository;
import com.yummie.security.TokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Value("${clientId}")
    private String clientId;

    public LoginResponse processLogin(UserEntity userEntity,
                                      HttpServletResponse response) {
        tokenUtil.setContext(userEntity);
        LoginResponse loginResponse = new LoginResponse();
        String accessToken = tokenUtil.generateJwt(userEntity);
        tokenUtil.setCookie("access_token", accessToken, response);
        loginResponse.setAccessToken(accessToken);
        RefreshTokenEntity refeshTokenEntity = refreshTokenService.createRefreshToken(userEntity);
        tokenUtil.setCookie("refesh_token", refeshTokenEntity.getValue(), response);
        loginResponse.setRefreshToken(refeshTokenEntity.getValue());
        return loginResponse;
    }

    public ResponseEntity<?> login(LoginRequest request,
                                   HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserEntity userEntity = userRepository.findByEmail(request.getEmail());
        LoginResponse loginResponse = processLogin(userEntity, response);
        return ResponseEntity.ok(loginResponse);
    }

    public ResponseEntity<?> loginWithGoogle(GoogleLoginRequest request,
                                             HttpServletResponse response)
            throws GeneralSecurityException, IOException {
        String credential = request.getCredential();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance()
        ).setAudience(List.of(clientId)).build();
        GoogleIdToken idToken = verifier.verify(credential);
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        if (!userRepository.existsByEmail(email)) userService.createUserFromGoogle(payload);
        UserEntity userEntity = userRepository.findByEmail(email);
        LoginResponse loginResponse = processLogin(userEntity, response);
        return ResponseEntity.ok(loginResponse);
    }
}
