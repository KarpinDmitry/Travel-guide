package ru.karpin.reviewsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karpin.commondto.client.AttractionClient;
import ru.karpin.reviewsservice.dto.CreateReviewDto;
import ru.karpin.reviewsservice.dto.ResponseReviewDto;
import ru.karpin.reviewsservice.exception.ReviewNotFoundException;
import ru.karpin.reviewsservice.mapper.ReviewMapper;
import ru.karpin.reviewsservice.repository.ReviewRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AttractionClient attractionClient;

    public ResponseReviewDto create(CreateReviewDto dto) {
        var review = reviewRepository.save(ReviewMapper.toReview(dto));
        updateAttractionRating(review.getAttractionId());
        return ReviewMapper.toResponseReviewDto(review);
    }

    @Transactional(readOnly = true)
    public List<ResponseReviewDto> getByAttractionId(Long attractionId) {
        return reviewRepository.findByAttractionId(attractionId).stream()
                .map(ReviewMapper::toResponseReviewDto)
                .toList();
    }

    public void delete(Long id) {
        var review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
        reviewRepository.delete(review);
        updateAttractionRating(review.getAttractionId());
    }

    private void updateAttractionRating(Long attractionId) {
        BigDecimal avg = reviewRepository.findAverageRatingByAttractionId(attractionId);
        attractionClient.updateAverageRating(attractionId, avg != null ? avg : BigDecimal.ZERO);
    }
}
