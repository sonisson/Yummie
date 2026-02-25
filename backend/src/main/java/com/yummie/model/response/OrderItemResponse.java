package com.yummie.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OrderItemResponse {
    private long id;
    private long quantity;
    private long price;
    private String productName;
    private String productImageUrl;
    private Map<String, String> attributes;
}
