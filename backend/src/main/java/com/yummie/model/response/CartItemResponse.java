package com.yummie.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class CartItemResponse {
    private long id;
    private String productName;
    private long price;
    private long quantity;
    private long productId;
    private String productImageUrl;
    private Map<String, Set<String>> productAttributes;
    private Map<String, String> variantAttributes;
}
