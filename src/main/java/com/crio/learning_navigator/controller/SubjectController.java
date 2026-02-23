package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.request.SubjectRequest;
import com.crio.learning_navigator.dto.response.SubjectResponse;
import com.crio.learning_navigator.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

  private final SubjectService subjectService;

  @PostMapping
  public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.createSubject(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable Long id) {
    return ResponseEntity.ok(subjectService.getSubjectById(id));
  }

  @GetMapping
  public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
    return ResponseEntity.ok(subjectService.getAllSubjects());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
    subjectService.deleteSubject(id);
    return ResponseEntity.noContent().build();
  }
}