package com.yummie.controller.auth;

import com.yummie.model.request.RegisterRequest;
import com.yummie.model.request.VerificationRequest;
import com.yummie.service.RegisterService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return registerService.register(request);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestBody VerificationRequest request,
                                           HttpServletResponse response) {
        return registerService.verifyAccount(request, response);
    }
}
