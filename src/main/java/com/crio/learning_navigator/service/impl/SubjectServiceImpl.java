package com.crio.learning_navigator.service.impl;

import com.crio.learning_navigator.dto.request.SubjectRequest;
import com.crio.learning_navigator.dto.response.SubjectResponse;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.mapper.SubjectMapper;
import com.crio.learning_navigator.repository.SubjectRepository;
import com.crio.learning_navigator.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

  private final SubjectRepository subjectRepository;
  private final SubjectMapper subjectMapper;

  @Override
  @Transactional
  public SubjectResponse createSubject(SubjectRequest request) {
    Subject subject = subjectMapper.toEntity(request);
    return subjectMapper.toResponse(subjectRepository.save(subject));
  }

  @Override
  @Transactional(readOnly = true)
  public SubjectResponse getSubjectById(Long id) {
    Subject subject = findSubjectOrThrow(id);
    return subjectMapper.toResponse(subject);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SubjectResponse> getAllSubjects() {
    return subjectRepository.findAll()
        .stream()
        .map(subjectMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void deleteSubject(Long id) {
    Subject subject = findSubjectOrThrow(id);
    subjectRepository.delete(subject);
  }

  private Subject findSubjectOrThrow(Long id) {
    return subjectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Subject with id: " + id + " not found"));
  }
}