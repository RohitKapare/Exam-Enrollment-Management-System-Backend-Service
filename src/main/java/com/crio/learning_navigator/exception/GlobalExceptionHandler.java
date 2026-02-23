package com.crio.learning_navigator.exception;

import com.crio.learning_navigator.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(DuplicateEnrollmentException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateEnrollment(DuplicateEnrollmentException ex) {
    return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(SubjectNotEnrolledException.class)
  public ResponseEntity<ErrorResponse> handleSubjectNotEnrolled(SubjectNotEnrolledException ex) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .collect(Collectors.joining(", "));
    return buildResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
  }

  private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
    ErrorResponse error = ErrorResponse.builder()
        .status(status.value())
        .message(message)
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, status);
  }
}