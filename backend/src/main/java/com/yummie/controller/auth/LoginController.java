package com.yummie.controller.auth;

import com.yummie.model.request.GoogleLoginRequest;
import com.yummie.model.request.LoginRequest;
import com.yummie.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auth/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request,
                                   HttpServletResponse response) {
        return loginService.login(request, response);
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleLoginRequest request,
                                             HttpServletResponse response) throws GeneralSecurityException, IOException {
        return loginService.loginWithGoogle(request, response);
    }
}
