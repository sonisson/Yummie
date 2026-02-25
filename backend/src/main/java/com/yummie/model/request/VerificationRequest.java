package com.yummie.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {
    private String code;
    private String email;
    private String password;
}
