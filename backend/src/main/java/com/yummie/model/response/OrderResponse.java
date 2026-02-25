package com.yummie.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private long id;
    private List<OrderItemResponse> items;
}
