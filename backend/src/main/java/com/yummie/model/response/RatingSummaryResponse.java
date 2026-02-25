package com.yummie.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RatingSummaryResponse {
    long total;
    long rating0;
    long rating1;
    long rating2;
    long rating3;
    long rating4;
    long rating5;
}
