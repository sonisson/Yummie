package com.yummie.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponse {
    long id;
    int rating;
    String comment;
    LocalDateTime createdAt;
    String userEmail;
}
