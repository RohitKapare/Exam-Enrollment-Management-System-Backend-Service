package com.crio.learning_navigator.exception;

public class SubjectNotEnrolledException extends RuntimeException {
  public SubjectNotEnrolledException(String message) {
    super(message);
  }
}