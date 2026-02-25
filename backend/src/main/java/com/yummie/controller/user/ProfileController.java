package com.yummie.controller.user;

import com.yummie.model.request.UpdateProfileRequest;
import com.yummie.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return profileService.getProfile(email);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(Authentication authentication,
                                           @ModelAttribute UpdateProfileRequest request,
                                           @RequestParam(value = "image", required = false) MultipartFile image) {
        String email = (String) authentication.getPrincipal();
        return profileService.updateProfile(email, request, image);
    }
}
