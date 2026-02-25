package com.yummie.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    private long variantId;
    private int quantity;
}
