package com.yummie.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryInfoResponse {
    private long id;
    private String name;
    private String province;
    private String ward;
    private String tel;
    private String detailAddress;
    private double longitude;
    private double latitude;
    private boolean isDefault;
}
