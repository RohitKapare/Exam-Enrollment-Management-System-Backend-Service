package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.request.StudentRequest;
import com.crio.learning_navigator.dto.response.ExamResponse;
import com.crio.learning_navigator.dto.response.StudentResponse;
import com.crio.learning_navigator.dto.response.SubjectResponse;
import com.crio.learning_navigator.exception.DuplicateEnrollmentException;
import com.crio.learning_navigator.exception.GlobalExceptionHandler;
import com.crio.learning_navigator.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock
  private StudentService studentService;

  @InjectMocks
  private StudentController studentController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(studentController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Should return 201 status with student response")
  void createStudent() throws Exception {
    StudentRequest request = new StudentRequest("Akash");
    StudentResponse response = StudentResponse.builder()
        .id(1L)
        .name("Akash")
        .enrolledSubjects(new ArrayList<>())
        .enrolledExams(new ArrayList<>())
        .build();

    when(studentService.createStudent(any(StudentRequest.class))).thenReturn(response);

    mockMvc.perform(post("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Akash"))
        .andExpect(jsonPath("$.enrolledSubjects").isArray())
        .andExpect(jsonPath("$.enrolledExams").isArray())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  @DisplayName("Should return 200 status with student response")
  void getStudentById() throws Exception {
    SubjectResponse subjectResponse = SubjectResponse.builder().id(1L).subjectName("ENGLISH").build();
    StudentResponse response = StudentResponse.builder()
        .id(1L)
        .name("Akash")
        .enrolledSubjects(List.of(subjectResponse))
        .enrolledExams(new ArrayList<>())
        .build();

    when(studentService.getStudentById(1L)).thenReturn(response);

    mockMvc.perform(get("/students/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Akash"))
        .andExpect(jsonPath("$.enrolledSubjects[0].subjectName").value("ENGLISH"))
        .andExpect(jsonPath("$.enrolledExams").isArray());
  }

  @Test
  @DisplayName("Should return 200 status with list")
  void getAllStudents() throws Exception {
    StudentResponse s1 = StudentResponse.builder()
        .id(1L).name("Akash")
        .enrolledSubjects(new ArrayList<>())
        .enrolledExams(new ArrayList<>())
        .build();

    when(studentService.getAllStudents()).thenReturn(List.of(s1));

    mockMvc.perform(get("/students"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].name").value("Akash"));
  }

  @Test
  @DisplayName("Should return 200 status with updated student response")
  void enrollInSubject() throws Exception {
    SubjectResponse subjectResponse = SubjectResponse.builder().id(1L).subjectName("ENGLISH").build();
    StudentResponse response = StudentResponse.builder()
        .id(1L)
        .name("Akash")
        .enrolledSubjects(List.of(subjectResponse))
        .enrolledExams(new ArrayList<>())
        .build();

    when(studentService.enrollInSubject(1L, 1L)).thenReturn(response);

    mockMvc.perform(post("/students/1/subjects/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Akash"))
        .andExpect(jsonPath("$.enrolledSubjects[0].subjectName").value("ENGLISH"))
        .andExpect(jsonPath("$.enrolledExams").isArray());
  }

  @Test
  @DisplayName("Should return 409 status when selected same subject again")
  void enrollInAlreadySelectedSubject() throws Exception {
    when(studentService.enrollInSubject(1L, 1L))
        .thenThrow(new DuplicateEnrollmentException("Student with id: 1 has already enrolled in subject"));

    mockMvc.perform(post("/students/1/subjects/1"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Student with id: 1 has already enrolled in subject"));
  }

  @Test
  @DisplayName("Should return 200 status when updated with student response")
  void enrollInExam() throws Exception {
    SubjectResponse subjectResponse = SubjectResponse.builder().id(1L).subjectName("ENGLISH").build();
    ExamResponse examResponse = ExamResponse.builder().id(1L).examName("ENGLISH Exam").build();
    StudentResponse response = StudentResponse.builder()
        .id(1L)
        .name("Akash")
        .enrolledSubjects(List.of(subjectResponse))
        .enrolledExams(List.of(examResponse))
        .build();

    when(studentService.enrollInExam(1L, 1L)).thenReturn(response);

    mockMvc.perform(post("/students/1/exams/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Akash"))
        .andExpect(jsonPath("$.enrolledExams[0].examName").value("ENGLISH Exam"));
  }

  @Test
  @DisplayName("Should return 409 status when enrolled is same exam again")
  void enrollInAlreadyEnrolledExam() throws Exception {
    when(studentService.enrollInExam(1L, 1L))
        .thenThrow(new DuplicateEnrollmentException(
            "Student with id: 1 has already enrolled for this particular exam with id: 1"));

    mockMvc.perform(post("/students/1/exams/1"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message")
            .value("Student with id: 1 has already enrolled for this particular exam with id: 1"));
  }
}