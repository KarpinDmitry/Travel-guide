package ru.karpin.attraction.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.karpin.attraction.entity.AttractionCategory;

public record CreateAttractionDto(@NotBlank String name, @NotNull AttractionCategory category, @NotBlank String city,
                                  @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
                                  @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
                                  String description) {
}
