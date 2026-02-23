package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.request.StudentRequest;
import com.crio.learning_navigator.dto.response.StudentResponse;
import com.crio.learning_navigator.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;

  @PostMapping
  public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
    return ResponseEntity.ok(studentService.getStudentById(id));
  }

  @GetMapping
  public ResponseEntity<List<StudentResponse>> getAllStudents() {
    return ResponseEntity.ok(studentService.getAllStudents());
  }

  @PostMapping("/{studentId}/subjects/{subjectId}")
  public ResponseEntity<StudentResponse> enrollInSubject(
      @PathVariable Long studentId,
      @PathVariable Long subjectId) {
    return ResponseEntity.ok(studentService.enrollInSubject(studentId, subjectId));
  }

  @PostMapping("/{studentId}/exams/{examId}")
  public ResponseEntity<StudentResponse> enrollInExam(
      @PathVariable Long studentId,
      @PathVariable Long examId) {
    return ResponseEntity.ok(studentService.enrollInExam(studentId, examId));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
    studentService.deleteStudent(id);
    return ResponseEntity.noContent().build();
  }
}