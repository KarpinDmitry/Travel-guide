package ru.karpin.attraction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.karpin.attraction.entity.Attraction;

import java.util.List;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    @Query(value = """
        SELECT * FROM attractions
        WHERE distance_km(:lat, :lon, latitude, longitude) < :radius
        AND (:category IS NULL OR category = :category)
        AND (:rating IS NULL OR average_rating >= :rating)
        ORDER BY distance_km(:lat, :lon, latitude, longitude)
        LIMIT :limit
        """, nativeQuery = true)
    List<Attraction> findNearbyOrderByDistance(@Param("lat") double lat, @Param("lon") double lon,
                                               @Param("radius") double radius, @Param("category") String category,
                                               @Param("rating") Float rating, @Param("limit") int limit);

    @Query(value = """
        SELECT * FROM attractions
        WHERE distance_km(:lat, :lon, latitude, longitude) < :radius
        AND (:category IS NULL OR category = :category)
        AND (:rating IS NULL OR average_rating >= :rating)
        ORDER BY average_rating DESC NULLS LAST
        LIMIT :limit
        """, nativeQuery = true)
    List<Attraction> findNearbyOrderByRating(@Param("lat") double lat, @Param("lon") double lon,
                                             @Param("radius") double radius, @Param("category") String category,
                                             @Param("rating") Float rating, @Param("limit") int limit);
}
