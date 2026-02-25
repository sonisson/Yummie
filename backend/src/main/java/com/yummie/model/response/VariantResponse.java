package com.yummie.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter

public class VariantResponse {
    Map<String, String> attributes;
    private long id;
    private long price;
}
