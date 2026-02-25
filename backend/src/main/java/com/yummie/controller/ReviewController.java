package com.yummie.controller;

import com.yummie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> getReviewList(@RequestParam("product-id") long productId,
                                           @RequestParam int star,
                                           @RequestParam String time,
                                           @RequestParam int page) {
        return reviewService.getReviewList(productId, star, time, page);
    }

    @GetMapping("/summary")
    public ResponseEntity<?> summaryRating(@RequestParam("product-id") long productId) {
        return reviewService.getRatingSummary(productId);
    }
}

