package com.yummie.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileResponse {
    private long id;
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String tel;
    private String imageUrl;
    private String email;
}
