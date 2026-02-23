package com.crio.learning_navigator.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {

  @NotBlank(message = "Student name must not be blank")
  private String name;
}