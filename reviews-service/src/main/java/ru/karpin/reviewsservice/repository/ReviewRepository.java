package ru.karpin.reviewsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.karpin.reviewsservice.entity.Review;

import java.math.BigDecimal;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAttractionId(Long attractionId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.attractionId = :attractionId")
    BigDecimal findAverageRatingByAttractionId(@Param("attractionId") Long attractionId);
}
