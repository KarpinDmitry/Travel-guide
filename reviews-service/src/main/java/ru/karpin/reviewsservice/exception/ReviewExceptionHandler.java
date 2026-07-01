package ru.karpin.reviewsservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ReviewNotFoundException exception, HttpServletRequest request) {
        return ErrorResponse.builder()
                .error("ReviewNotFoundException")
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(404)
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ErrorResponse.builder()
                .error("MethodArgumentNotValidException")
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(400)
                .message(message)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException exception, HttpServletRequest request) {
        String message = exception.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        return ErrorResponse.builder()
                .error("ConstraintViolationException")
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(400)
                .message(message)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotReadable(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return ErrorResponse.builder()
                .error("HttpMessageNotReadableException")
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(400)
                .message(exception.getMostSpecificCause().getMessage())
                .build();
    }
}
