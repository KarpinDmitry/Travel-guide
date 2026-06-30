package ru.karpin.attraction.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.karpin.attraction.dto.CreateAttractionDto;
import ru.karpin.attraction.dto.ResponseAttractionDto;
import ru.karpin.attraction.dto.UpdateAttractionDto;
import ru.karpin.attraction.entity.Attraction;
import ru.karpin.attraction.entity.AttractionCategory;
import ru.karpin.attraction.exception.EntityNotFoundException;
import ru.karpin.attraction.repository.AttractionRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private AttractionService attractionService;

    private Attraction buildAttraction() {
        return Attraction.builder()
                .id(1L)
                .name("Эрмитаж")
                .category(AttractionCategory.MUSEUM)
                .city("Санкт-Петербург")
                .latitude(59.9398)
                .longitude(30.3146)
                .description("Главный музей России")
                .build();
    }

    @Test
    void create_shouldReturnDto() {
        CreateAttractionDto dto = new CreateAttractionDto(
                "Эрмитаж", AttractionCategory.MUSEUM, "Санкт-Петербург",
                59.9398, 30.3146, "Главный музей России");
        Attraction saved = buildAttraction();

        when(attractionRepository.save(any(Attraction.class))).thenReturn(saved);

        ResponseAttractionDto result = attractionService.create(dto);

        assertThat(result.name()).isEqualTo("Эрмитаж");
        assertThat(result.category()).isEqualTo(AttractionCategory.MUSEUM);
        assertThat(result.city()).isEqualTo("Санкт-Петербург");
    }

    @Test
    void getById_shouldReturnDto_whenFound() {
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(buildAttraction()));

        ResponseAttractionDto result = attractionService.getById(1L);

        assertThat(result.name()).isEqualTo("Эрмитаж");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(attractionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attractionService.getById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void update_shouldReturnUpdatedDto_whenFound() {
        UpdateAttractionDto dto = new UpdateAttractionDto(1L, "Новое название", null, null, null, null, null);
        Attraction existing = buildAttraction();

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(attractionRepository.save(any(Attraction.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseAttractionDto result = attractionService.update(dto);

        assertThat(result.name()).isEqualTo("Новое название");
        assertThat(result.category()).isEqualTo(AttractionCategory.MUSEUM);
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        UpdateAttractionDto dto = new UpdateAttractionDto(99L, "Название", null, null, null, null, null);
        when(attractionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attractionService.update(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findNearby_shouldReturnDtoList() {
        List<Attraction> attractions = List.of(buildAttraction());
        when(attractionRepository.findNearby(59.9, 30.3, 5.0, null, null, 10))
                .thenReturn(attractions);

        List<ResponseAttractionDto> result = attractionService.findNearby(59.9, 30.3, 5.0, null, null, 10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Эрмитаж");
    }

    @Test
    void findNearby_shouldPassCategoryAndRating() {
        List<Attraction> attractions = List.of(buildAttraction());
        when(attractionRepository.findNearby(59.9, 30.3, 5.0, "MUSEUM", 4.0f, 5))
                .thenReturn(attractions);

        List<ResponseAttractionDto> result = attractionService.findNearby(
                59.9, 30.3, 5.0, AttractionCategory.MUSEUM, 4.0f, 5);

        assertThat(result).hasSize(1);
    }
}
