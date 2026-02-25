package com.yummie.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryInfoRequest {
    private String name;
    private String tel;
    private String province;
    private String ward;
    private String detailAddress;
    private double longitude;
    private double latitude;
    private boolean isDefault;
}
