package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.request.SubjectRequest;
import com.crio.learning_navigator.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {
  SubjectResponse createSubject(SubjectRequest request);

  SubjectResponse getSubjectById(Long id);

  List<SubjectResponse> getAllSubjects();

  void deleteSubject(Long id);
}