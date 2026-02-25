package com.yummie.controller.user;

import com.yummie.model.request.DeliveryInfoRequest;
import com.yummie.service.DeliveryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/delivery-info")
@RequiredArgsConstructor
public class DeliveryInfoController {

    private final DeliveryInfoService deliveryInfoService;

    @GetMapping
    public ResponseEntity<?> getDeliveryInfoList(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return deliveryInfoService.getDeliveryInfoResponseList(email);
    }

    @GetMapping("/default")
    public ResponseEntity<?> getDefaultDeliveryInfo(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return deliveryInfoService.getDefaultDeliveryInfo(email);
    }

    @PostMapping
    public ResponseEntity<?> createDeliveryInfo(Authentication authentication,
                                                @RequestBody DeliveryInfoRequest request) {
        String email = (String) authentication.getPrincipal();
        return deliveryInfoService.createDeliveryInfo(email, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDeliveryInfo(Authentication authentication,
                                                @PathVariable long id,
                                                @RequestBody DeliveryInfoRequest request) {
        String email = (String) authentication.getPrincipal();
        return deliveryInfoService.updateDeliveryInfo(email, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeliveryInfo(Authentication authentication,
                                                @PathVariable long id) {
        String email = (String) authentication.getPrincipal();
        return deliveryInfoService.deleteDeliveryInfo(email, id);
    }
}
