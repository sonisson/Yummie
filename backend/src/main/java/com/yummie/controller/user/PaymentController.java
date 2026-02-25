package com.yummie.controller.user;

import com.yummie.model.request.PayRequest;
import com.yummie.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(Authentication authentication,
                                           @RequestBody PayRequest payRequest,
                                           HttpServletRequest request) {
        String email = (String) authentication.getPrincipal();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return paymentService.generatePaymentUrl(payRequest.getOrderId(), email, ipAddress);
    }

    @GetMapping("/ipn")
    public String handleIpn(@RequestParam Map<String, String> params) {
        return paymentService.processPaymentIpn(params);
    }
}
