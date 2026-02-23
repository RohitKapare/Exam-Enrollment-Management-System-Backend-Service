package com.crio.learning_navigator.service.impl;

import com.crio.learning_navigator.dto.request.StudentRequest;
import com.crio.learning_navigator.dto.response.StudentResponse;
import com.crio.learning_navigator.entity.Exam;
import com.crio.learning_navigator.entity.Student;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.exception.DuplicateEnrollmentException;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.exception.SubjectNotEnrolledException;
import com.crio.learning_navigator.mapper.StudentMapper;
import com.crio.learning_navigator.repository.ExamRepository;
import com.crio.learning_navigator.repository.StudentRepository;
import com.crio.learning_navigator.repository.SubjectRepository;
import com.crio.learning_navigator.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;
  private final SubjectRepository subjectRepository;
  private final ExamRepository examRepository;
  private final StudentMapper studentMapper;

  @Override
  @Transactional
  public StudentResponse createStudent(StudentRequest request) {
    Student student = studentMapper.toEntity(request);
    return studentMapper.toResponse(studentRepository.save(student));
  }

  @Override
  @Transactional(readOnly = true)
  public StudentResponse getStudentById(Long id) {
    Student student = findStudentOrThrow(id);
    return studentMapper.toResponse(student);
  }

  @Override
  @Transactional(readOnly = true)
  public List<StudentResponse> getAllStudents() {
    return studentRepository.findAll()
        .stream()
        .map(studentMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public StudentResponse enrollInSubject(Long studentId, Long subjectId) {
    Student student = findStudentOrThrow(studentId);
    Subject subject = findSubjectOrThrow(subjectId);

    boolean alreadyEnrolled = student.getEnrolledSubjects()
        .stream()
        .anyMatch(s -> s.getId().equals(subjectId));

    if (alreadyEnrolled) {
      throw new DuplicateEnrollmentException(
          "Student with id: " + studentId + " has already enrolled in subject");
    }

    student.getEnrolledSubjects().add(subject);
    return studentMapper.toResponse(studentRepository.save(student));
  }

  @Override
  @Transactional
  public StudentResponse enrollInExam(Long studentId, Long examId) {
    Student student = findStudentOrThrow(studentId);
    Exam exam = findExamOrThrow(examId);

    // Business rule: student must be enrolled in the exam's subject
    Long examSubjectId = exam.getSubject().getId();
    boolean enrolledInSubject = student.getEnrolledSubjects()
        .stream()
        .anyMatch(s -> s.getId().equals(examSubjectId));

    if (!enrolledInSubject) {
      throw new SubjectNotEnrolledException(
          "Student with id: " + studentId + " is not enrolled in the subject for exam id: " + examId);
    }

    // Business rule: prevent duplicate exam registration
    boolean alreadyRegistered = student.getEnrolledExams()
        .stream()
        .anyMatch(e -> e.getId().equals(examId));

    if (alreadyRegistered) {
      throw new DuplicateEnrollmentException(
          "Student with id: " + studentId + " has already enrolled for this particular exam with id: " + examId);
    }

    student.getEnrolledExams().add(exam);
    return studentMapper.toResponse(studentRepository.save(student));
  }

  @Override
  @Transactional
  public void deleteStudent(Long id) {
    Student student = findStudentOrThrow(id);
    studentRepository.delete(student);
  }

  // ── Private helpers ──────────────────────────────────────────────────────

  private Student findStudentOrThrow(Long id) {
    return studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student with id: " + id + " not found"));
  }

  private Subject findSubjectOrThrow(Long id) {
    return subjectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Subject with id: " + id + " not found"));
  }

  private Exam findExamOrThrow(Long id) {
    return examRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Exam with id: " + id + " not found"));
  }
}