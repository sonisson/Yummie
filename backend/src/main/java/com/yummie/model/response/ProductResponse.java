package com.yummie.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ProductResponse {
    private long id;
    private String name;
    private String imageUrl;
    private Double rating;
    private Map<String, Set<String>> attributes;
    private List<VariantResponse> variants;
}
