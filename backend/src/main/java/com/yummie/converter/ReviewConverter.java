package com.yummie.converter;

import com.yummie.entity.ReviewEntity;
import com.yummie.model.response.PageResponse;
import com.yummie.model.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewConverter {

    private final ModelMapper modelMapper;

    public ReviewResponse toReviewResponse(ReviewEntity reviewEntity) {
        ReviewResponse reviewResponse = modelMapper.map(reviewEntity, ReviewResponse.class);
        reviewResponse.setUserEmail(reviewEntity.getUserEntity().getEmail());
        return reviewResponse;
    }

    public List<ReviewResponse> toReviewResponseList(List<ReviewEntity> reviewEntities) {
        return reviewEntities.stream().map(this::toReviewResponse).toList();
    }

    public PageResponse<ReviewResponse> toReviewResponsePage(Page<ReviewEntity> reviewEntityPage) {
        return new PageResponse<>(toReviewResponseList(reviewEntityPage.getContent()),
                reviewEntityPage.getTotalPages(),
                reviewEntityPage.getNumber() + 1);
    }
}
