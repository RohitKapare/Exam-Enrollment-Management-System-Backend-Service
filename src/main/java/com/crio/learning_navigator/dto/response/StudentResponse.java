package com.crio.learning_navigator.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
  private Long id;
  private String name;
  private List<SubjectResponse> enrolledSubjects;
  private List<ExamResponse> enrolledExams;
}