package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.response.ExamResponse;

import java.util.List;

public interface ExamService {
  ExamResponse createExam(Long subjectId);

  ExamResponse getExamById(Long id);

  List<ExamResponse> getAllExams();

  void deleteExam(Long id);
}