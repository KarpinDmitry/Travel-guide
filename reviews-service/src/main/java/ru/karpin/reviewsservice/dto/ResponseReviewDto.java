package ru.karpin.reviewsservice.dto;

import java.time.OffsetDateTime;

public record ResponseReviewDto(
        Long id,
        Long attractionId,
        String author,
        Byte rating,
        String text,
        OffsetDateTime createdAt
) {
}
