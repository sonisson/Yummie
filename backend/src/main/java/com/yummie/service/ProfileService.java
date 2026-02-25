package com.yummie.service;

import com.yummie.converter.ProfileConverter;
import com.yummie.entity.ProfileEntity;
import com.yummie.model.request.UpdateProfileRequest;
import com.yummie.repository.ProfileRepository;
import com.yummie.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileConverter profileConverter;
    private final ImageUtil imageUtil;

    public ResponseEntity<?> getProfile(String email) {
        ProfileEntity profileEntity = profileRepository.findByUserEntityEmail(email);
        return ResponseEntity.ok(profileConverter.toProfileResponse(profileEntity));
    }

    public ResponseEntity<?> updateProfile(String email,
                                           UpdateProfileRequest request,
                                           MultipartFile image) {
        ProfileEntity profileEntity = profileRepository.findByUserEntityEmail(email);
        if (image != null) {
            String fileName = profileEntity.getId() + ".jpg";
            String imageUrl = imageUtil.uploadImage(image, "avatar", fileName);
            request.setImageUrl(imageUrl);
        } else {
            request.setImageUrl(profileEntity.getImageUrl());
        }
        profileConverter.toProfileEntity(request, profileEntity);
        profileRepository.save(profileEntity);
        return ResponseEntity.noContent().build();
    }
}
