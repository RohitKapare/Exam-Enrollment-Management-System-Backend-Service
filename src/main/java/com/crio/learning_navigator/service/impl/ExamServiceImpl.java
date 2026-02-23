package com.crio.learning_navigator.service.impl;

import com.crio.learning_navigator.dto.response.ExamResponse;
import com.crio.learning_navigator.entity.Exam;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.mapper.ExamMapper;
import com.crio.learning_navigator.repository.ExamRepository;
import com.crio.learning_navigator.repository.SubjectRepository;
import com.crio.learning_navigator.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

  private final ExamRepository examRepository;
  private final SubjectRepository subjectRepository;
  private final ExamMapper examMapper;

  @Override
  @Transactional
  public ExamResponse createExam(Long subjectId) {
    Subject subject = subjectRepository.findById(subjectId)
        .orElseThrow(() -> new ResourceNotFoundException("Subject with id: " + subjectId + " not found"));

    Exam exam = Exam.builder()
        .subject(subject)
        .examName(subject.getSubjectName() + " Exam")
        .build();

    return examMapper.toResponse(examRepository.save(exam));
  }

  @Override
  @Transactional(readOnly = true)
  public ExamResponse getExamById(Long id) {
    Exam exam = findExamOrThrow(id);
    return examMapper.toResponse(exam);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ExamResponse> getAllExams() {
    return examRepository.findAll()
        .stream()
        .map(examMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void deleteExam(Long id) {
    Exam exam = findExamOrThrow(id);
    examRepository.delete(exam);
  }

  private Exam findExamOrThrow(Long id) {
    return examRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Exam with id: " + id + " not found"));
  }
}