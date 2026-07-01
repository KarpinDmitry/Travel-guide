package ru.karpin.reviewsservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateReviewDto(
        @NotNull Long attractionId,
        @NotBlank @Length(max = 100) String author,
        @NotNull @Min(1) @Max(5) Byte rating,
        @Length(max = 2000) String text
) {
}
