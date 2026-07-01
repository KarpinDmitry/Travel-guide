package ru.karpin.attraction.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.karpin.attraction.dto.CreateAttractionDto;
import ru.karpin.attraction.dto.ResponseAttractionDto;
import ru.karpin.attraction.dto.UpdateAttractionDto;
import ru.karpin.attraction.entity.AttractionCategory;
import ru.karpin.attraction.entity.AttractionSort;
import ru.karpin.attraction.service.AttractionService;
import java.math.BigDecimal;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/attraction")
@Slf4j
@Validated
public class AttractionController {
    private final AttractionService attractionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseAttractionDto create(@RequestBody @Valid CreateAttractionDto createDto){
        log.info("POST {}", createDto);
        return attractionService.create(createDto);
    }

    @PatchMapping
    public ResponseAttractionDto update(@RequestBody @Valid UpdateAttractionDto updateDto){
        log.info("PATCH {}", updateDto);
        return attractionService.update(updateDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseAttractionDto getById(@PathVariable @Positive Long id){
        log.info("GET /{id} {}", id);
        return attractionService.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseAttractionDto> findNearby(@RequestParam @DecimalMin("-90.0")
                                                      @DecimalMax("90.0") double latitude,
                                                  @RequestParam @DecimalMin("-180.0")
                                                  @DecimalMax("180.0") double longitude,
                                                  @RequestParam @Positive double radius,
                                                  @RequestParam(required = false) AttractionCategory category,
                                                  @RequestParam(required = false) Float minRating,
                                                  @RequestParam(required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(required = false, defaultValue = "DISTANCE") AttractionSort sort){
        log.info("GET latitude: {}, longitude: {}, radius: {}", latitude, longitude, radius);
        return attractionService.findNearby(latitude, longitude, radius, category, minRating, limit, sort);
    }

    @PatchMapping("/{id}/rating")
    public void updateAverageRating(@PathVariable("id") Long attractionId, @RequestParam BigDecimal averageRating) {
        log.info("PATCH /attraction/{}/rating = {}", attractionId, averageRating);
        attractionService.updateAverageRating(attractionId, averageRating);
    }
}
