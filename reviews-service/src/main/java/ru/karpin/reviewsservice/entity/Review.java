package ru.karpin.reviewsservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "attraction_id", nullable = false)
    @NotNull
    private Long attractionId;
    @Length(max = 100)
    @NotBlank
    @Column(nullable = false)
    private String author;
    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Byte rating;
    private String text;
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

}
