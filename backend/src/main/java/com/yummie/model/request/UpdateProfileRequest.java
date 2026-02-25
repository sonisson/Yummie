package com.yummie.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateProfileRequest {
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String tel;
    private String imageUrl;
}
