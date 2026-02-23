package com.crio.learning_navigator.mapper;

import com.crio.learning_navigator.dto.request.SubjectRequest;
import com.crio.learning_navigator.dto.response.SubjectResponse;
import com.crio.learning_navigator.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "enrolledStudents", ignore = true)
  Subject toEntity(SubjectRequest request);

  SubjectResponse toResponse(Subject subject);
}