package com.crio.learning_navigator.mapper;

import com.crio.learning_navigator.dto.response.ExamResponse;
import com.crio.learning_navigator.entity.Exam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamMapper {

  ExamResponse toResponse(Exam exam);
}