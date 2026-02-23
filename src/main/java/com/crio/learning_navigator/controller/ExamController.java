package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.response.ExamResponse;
import com.crio.learning_navigator.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

  private final ExamService examService;

  @PostMapping("/subjects/{subjectId}")
  public ResponseEntity<ExamResponse> createExam(@PathVariable Long subjectId) {
    return ResponseEntity.status(HttpStatus.CREATED).body(examService.createExam(subjectId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ExamResponse> getExamById(@PathVariable Long id) {
    return ResponseEntity.ok(examService.getExamById(id));
  }

  @GetMapping
  public ResponseEntity<List<ExamResponse>> getAllExams() {
    return ResponseEntity.ok(examService.getAllExams());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
    examService.deleteExam(id);
    return ResponseEntity.noContent().build();
  }
}