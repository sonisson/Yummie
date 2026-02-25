package com.yummie.repository;

import com.yummie.entity.ReviewEntity;
import com.yummie.model.response.RatingSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Page<ReviewEntity> findByProductEntityIdAndRating(long productId, int star, Pageable pageable);

    Page<ReviewEntity> findByProductEntityId(long productId, Pageable pageable);

    @Query("""
                SELECT new com.yummie.model.response.RatingSummaryResponse(
                    COUNT(r),
                    SUM(CASE WHEN r.rating = 0 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 2 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 3 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 4 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN r.rating = 5 THEN 1 ELSE 0 END)
                )
                FROM ReviewEntity r
                WHERE r.productEntity.id = :productId
            """)
    RatingSummaryResponse getRatingSummary(@Param("productId") long productId);
}
