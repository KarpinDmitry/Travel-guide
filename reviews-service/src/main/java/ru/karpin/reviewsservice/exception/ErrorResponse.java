package ru.karpin.reviewsservice.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(LocalDateTime time, int status, String error, String message, String path) {
}
