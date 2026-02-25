package com.yummie.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UpdateCartItemRequest {
    private Integer quantity;
    private Map<String, String> attributes;
}
