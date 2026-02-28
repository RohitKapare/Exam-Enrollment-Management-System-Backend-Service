package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.request.SubjectRequest;
import com.crio.learning_navigator.dto.response.SubjectResponse;
import com.crio.learning_navigator.exception.GlobalExceptionHandler;
import com.crio.learning_navigator.service.SubjectService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock
  private SubjectService subjectService;

  @InjectMocks
  private SubjectController subjectController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(subjectController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Creating subject should return 201 status with subject response")
  void createSubject() throws Exception {
    SubjectRequest request = new SubjectRequest("ENGLISH");
    SubjectResponse response = SubjectResponse.builder().id(1L).subjectName("ENGLISH").build();

    when(subjectService.createSubject(any(SubjectRequest.class))).thenReturn(response);

    mockMvc.perform(post("/subjects")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.subjectName").value("ENGLISH"))
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  @DisplayName("Getting subject by id should return 200 status")
  void getSubjectById() throws Exception {
    SubjectResponse response = SubjectResponse.builder().id(1L).subjectName("ENGLISH").build();
    when(subjectService.getSubjectById(1L)).thenReturn(response);

    mockMvc.perform(get("/subjects/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.subjectName").value("ENGLISH"));
  }

  @Test
  @DisplayName("Should return 200 status with list")
  void getAllSubjects() throws Exception {
    SubjectResponse s = SubjectResponse.builder().id(1L).subjectName("ENGLISH").build();
    when(subjectService.getAllSubjects()).thenReturn(List.of(s));

    mockMvc.perform(get("/subjects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].subjectName").value("ENGLISH"));
  }

  @Test
  @DisplayName("Should return not content 204 status")
  void deleteSubject() throws Exception {
    doNothing().when(subjectService).deleteSubject(1L);

    mockMvc.perform(delete("/subjects/1"))
        .andExpect(status().isNoContent());
  }
}