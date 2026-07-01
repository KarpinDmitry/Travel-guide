package ru.karpin.reviewsservice.mapper;

import lombok.experimental.UtilityClass;
import ru.karpin.reviewsservice.dto.CreateReviewDto;
import ru.karpin.reviewsservice.dto.ResponseReviewDto;
import ru.karpin.reviewsservice.entity.Review;

@UtilityClass
public class ReviewMapper {

    public static Review toReview(CreateReviewDto dto) {
        return Review.builder()
                .attractionId(dto.attractionId())
                .author(dto.author())
                .rating(dto.rating())
                .text(dto.text())
                .build();
    }

    public static ResponseReviewDto toResponseReviewDto(Review review) {
        return new ResponseReviewDto(
                review.getId(),
                review.getAttractionId(),
                review.getAuthor(),
                review.getRating(),
                review.getText(),
                review.getCreatedAt()
        );
    }
}
