package ru.karpin.attraction.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.karpin.attraction.dto.CreateAttractionDto;
import ru.karpin.attraction.dto.ResponseAttractionDto;
import ru.karpin.attraction.dto.UpdateAttractionDto;
import ru.karpin.attraction.entity.Attraction;
import ru.karpin.attraction.entity.AttractionCategory;
import ru.karpin.attraction.exception.EntityNotFoundException;
import ru.karpin.attraction.mapper.AttractionMapper;
import ru.karpin.attraction.repository.AttractionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class AttractionService {
    private final AttractionRepository attractionRepository;

    public ResponseAttractionDto create(@Valid CreateAttractionDto createDto){
        Attraction attraction = AttractionMapper.toAttraction(createDto);

        attraction = attractionRepository.save(attraction);

        return AttractionMapper.toResponseAttractionDto(attraction);
    }

    public ResponseAttractionDto update(@Valid UpdateAttractionDto updateDto){
        Attraction attraction = attractionRepository.findById(updateDto.id())
                .orElseThrow(() -> new EntityNotFoundException("Attraction with id " + updateDto.id() + " not found"));

        Attraction updateAttraction = AttractionMapper.toAttraction(updateDto, attraction);
        return AttractionMapper.toResponseAttractionDto(attractionRepository.save(updateAttraction));
    }

    @Transactional(readOnly = true)
    public ResponseAttractionDto getById(@NotNull Long id){
        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attraction with id " + id + " not found"));

        return AttractionMapper.toResponseAttractionDto(attraction);
    }

    public List<ResponseAttractionDto> findNearby(@NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
                                                  @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
                                                  @NotNull Double radius, AttractionCategory category,
                                                  Float minRating, Integer limit){
        List<Attraction> attractionList = attractionRepository
                .findNearby(latitude, longitude, radius, category != null ? category.name() : null, minRating, limit);

        return attractionList.stream()
                .map(AttractionMapper::toResponseAttractionDto)
                .toList();
    }
}
