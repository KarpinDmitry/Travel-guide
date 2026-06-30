package ru.karpin.attraction.dto;

import ru.karpin.attraction.entity.AttractionCategory;

import java.math.BigDecimal;

public record ResponseAttractionDto(String name, AttractionCategory category, String city,
                                    Double latitude, Double longitude, String description, BigDecimal averageRating) {
}
