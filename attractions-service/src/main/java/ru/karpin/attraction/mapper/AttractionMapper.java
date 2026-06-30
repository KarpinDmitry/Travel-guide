package ru.karpin.attraction.mapper;

import lombok.experimental.UtilityClass;
import ru.karpin.attraction.dto.CreateAttractionDto;
import ru.karpin.attraction.dto.ResponseAttractionDto;
import ru.karpin.attraction.dto.UpdateAttractionDto;
import ru.karpin.attraction.entity.Attraction;

@UtilityClass
public class AttractionMapper {
    public static Attraction toAttraction(CreateAttractionDto createDto){
        return Attraction.builder().name(createDto.name())
                .city(createDto.city())
                .description(createDto.description())
                .latitude(createDto.latitude())
                .longitude(createDto.longitude())
                .category(createDto.category())
                .build();
    }

    public static ResponseAttractionDto toResponseAttractionDto(Attraction attraction) {
        return new ResponseAttractionDto(attraction.getName(), attraction.getCategory(), attraction.getCity(),
                attraction.getLatitude(), attraction.getLongitude(),
                attraction.getDescription(), attraction.getAverageRating());
    }

    public static Attraction toAttraction(UpdateAttractionDto updateDto, Attraction attraction) {

        return new Attraction(
                attraction.getId(),
                updateDto.name() != null ? updateDto.name() : attraction.getName(),
                updateDto.category() != null ? updateDto.category() : attraction.getCategory(),
                updateDto.city() != null ? updateDto.city() : attraction.getCity(),
                updateDto.latitude() != null ? updateDto.latitude() : attraction.getLatitude(),
                updateDto.longitude() != null ? updateDto.longitude() : attraction.getLongitude(),
                updateDto.description() != null ? updateDto.description() : attraction.getDescription(),
                attraction.getCreatedAt(),
                attraction.getAverageRating()
        );
    }
}
