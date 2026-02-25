package com.yummie.service;

import com.yummie.converter.ReviewConverter;
import com.yummie.entity.ReviewEntity;
import com.yummie.repository.ReviewRepository;
import com.yummie.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final PageUtil pageUtil;

    public ResponseEntity<?> getReviewList(long productId, int star, String time, int page) {
        Sort sort;
        if ("oldest".equals(time)) sort = Sort.by("createdAt");
        else sort = Sort.by("createdAt").descending();
        Page<ReviewEntity> reviewEntityPage;
        if (star == -1) reviewEntityPage = pageUtil.getPage(page, 5, sort,
                p -> reviewRepository.findByProductEntityId(productId, p));
        else reviewEntityPage = pageUtil.getPage(page, 5, sort,
                p -> reviewRepository.findByProductEntityIdAndRating(productId, star, p));
        return ResponseEntity.ok(reviewConverter.toReviewResponsePage(reviewEntityPage));
    }

    public ResponseEntity<?> getRatingSummary(long productId) {
        return ResponseEntity.ok(reviewRepository.getRatingSummary(productId));
    }
}
