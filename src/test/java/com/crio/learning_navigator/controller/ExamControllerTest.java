package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.response.ExamResponse;
import com.crio.learning_navigator.exception.GlobalExceptionHandler;
import com.crio.learning_navigator.service.ExamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExamControllerTest {

  private MockMvc mockMvc;

  @Mock
  private ExamService examService;

  @InjectMocks
  private ExamController examController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(examController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Should return 201 with exam response")
  void createExam() throws Exception {
    ExamResponse response = ExamResponse.builder().id(1L).examName("ENGLISH Exam").build();
    when(examService.createExam(1L)).thenReturn(response);

    mockMvc.perform(post("/exams/subjects/1"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.examName").value("ENGLISH Exam"))
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  @DisplayName("Should return 200 status")
  void getExamById() throws Exception {
    ExamResponse response = ExamResponse.builder().id(1L).examName("ENGLISH Exam").build();
    when(examService.getExamById(1L)).thenReturn(response);

    mockMvc.perform(get("/exams/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.examName").value("ENGLISH Exam"));
  }

  @Test
  @DisplayName("Should return 200 status with list of exams")
  void getAllExams() throws Exception {
    ExamResponse e = ExamResponse.builder().id(1L).examName("ENGLISH Exam").build();
    when(examService.getAllExams()).thenReturn(List.of(e));

    mockMvc.perform(get("/exams"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].examName").value("ENGLISH Exam"));
  }

  @Test
  @DisplayName("Should return 204 no content status")
  void deleteExam() throws Exception {
    doNothing().when(examService).deleteExam(1L);

    mockMvc.perform(delete("/exams/1"))
        .andExpect(status().isNoContent());
  }
}