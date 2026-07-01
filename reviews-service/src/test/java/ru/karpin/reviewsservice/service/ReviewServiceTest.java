package ru.karpin.reviewsservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.karpin.commondto.client.AttractionClient;
import ru.karpin.reviewsservice.dto.CreateReviewDto;
import ru.karpin.reviewsservice.dto.ResponseReviewDto;
import ru.karpin.reviewsservice.entity.Review;
import ru.karpin.reviewsservice.exception.ReviewNotFoundException;
import ru.karpin.reviewsservice.mapper.ReviewMapper;
import ru.karpin.reviewsservice.repository.ReviewRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AttractionClient attractionClient;

    @InjectMocks
    private ReviewService reviewService;

    private Review buildReview() {
        return Review.builder()
                .id(1L)
                .attractionId(10L)
                .author("Иван Петров")
                .rating((byte) 5)
                .text("Отличное место")
                .build();
    }

    @Test
    void create_shouldSaveReviewAndUpdateAttractionRating() {
        CreateReviewDto dto = new CreateReviewDto(10L, "Иван Петров", (byte) 5, "Отличное место");
        Review saved = buildReview();

        when(reviewRepository.save(any(Review.class))).thenReturn(saved);
        when(reviewRepository.findAverageRatingByAttractionId(10L)).thenReturn(new BigDecimal("5.0"));

        ResponseReviewDto result = reviewService.create(dto);

        assertThat(result.attractionId()).isEqualTo(10L);
        assertThat(result.author()).isEqualTo("Иван Петров");
        assertThat(result.rating()).isEqualTo((byte) 5);
        verify(attractionClient).updateAverageRating(eq(10L), eq(new BigDecimal("5.0")));
    }

    @Test
    void create_shouldSendZeroRating_whenNoReviewsLeft() {
        CreateReviewDto dto = new CreateReviewDto(10L, "Иван Петров", (byte) 5, null);
        Review saved = buildReview();

        when(reviewRepository.save(any(Review.class))).thenReturn(saved);
        when(reviewRepository.findAverageRatingByAttractionId(10L)).thenReturn(null);

        reviewService.create(dto);

        verify(attractionClient).updateAverageRating(eq(10L), eq(BigDecimal.ZERO));
    }

    @Test
    void getByAttractionId_shouldReturnMappedList() {
        when(reviewRepository.findByAttractionId(10L)).thenReturn(List.of(buildReview()));

        List<ResponseReviewDto> result = reviewService.getByAttractionId(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).attractionId()).isEqualTo(10L);
    }

    @Test
    void delete_shouldDeleteReviewAndUpdateAttractionRating() {
        Review review = buildReview();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.findAverageRatingByAttractionId(10L)).thenReturn(new BigDecimal("4.0"));

        reviewService.delete(1L);

        verify(reviewRepository).delete(review);
        verify(attractionClient).updateAverageRating(eq(10L), eq(new BigDecimal("4.0")));
    }

    @Test
    void delete_shouldThrow_whenReviewNotFound() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.delete(99L))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining("99");
    }
}
