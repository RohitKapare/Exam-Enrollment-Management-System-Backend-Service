package com.crio.learning_navigator.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectResponse {
    private Long id;
    private String subjectName;
}