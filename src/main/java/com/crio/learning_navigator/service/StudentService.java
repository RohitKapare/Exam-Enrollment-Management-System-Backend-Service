package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.request.StudentRequest;
import com.crio.learning_navigator.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
  StudentResponse createStudent(StudentRequest request);

  StudentResponse getStudentById(Long id);

  List<StudentResponse> getAllStudents();

  StudentResponse enrollInSubject(Long studentId, Long subjectId);

  StudentResponse enrollInExam(Long studentId, Long examId);

  void deleteStudent(Long id);
}