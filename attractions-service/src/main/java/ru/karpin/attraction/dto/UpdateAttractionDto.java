package ru.karpin.attraction.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import ru.karpin.attraction.entity.AttractionCategory;

public record UpdateAttractionDto(@NotNull Long id, String name, AttractionCategory category, String city,
                                  @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
                                  @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
                                  String description) {
}
