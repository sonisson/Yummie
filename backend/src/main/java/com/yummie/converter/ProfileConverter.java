package com.yummie.converter;

import com.yummie.entity.ProfileEntity;
import com.yummie.model.request.UpdateProfileRequest;
import com.yummie.model.response.ProfileResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProfileConverter {

    private final ModelMapper modelMapper;

    public ProfileResponse toProfileResponse(ProfileEntity profileEntity) {
        ProfileResponse profileResponse = modelMapper.map(profileEntity, ProfileResponse.class);
        profileResponse.setEmail(profileEntity.getUserEntity().getEmail());
        return profileResponse;
    }

    public void toProfileEntity(UpdateProfileRequest request, ProfileEntity profileEntity) {
        modelMapper.map(request, profileEntity);
    }
}
