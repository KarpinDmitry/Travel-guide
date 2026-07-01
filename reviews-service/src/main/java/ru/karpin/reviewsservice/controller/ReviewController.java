package ru.karpin.reviewsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.karpin.reviewsservice.dto.CreateReviewDto;
import ru.karpin.reviewsservice.dto.ResponseReviewDto;
import ru.karpin.reviewsservice.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseReviewDto create(@RequestBody @Valid CreateReviewDto dto) {
        return reviewService.create(dto);
    }

    @GetMapping
    public List<ResponseReviewDto> getByAttractionId(@RequestParam Long attractionId) {
        return reviewService.getByAttractionId(attractionId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reviewService.delete(id);
    }
}
