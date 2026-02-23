package com.crio.learning_navigator.mapper;

import com.crio.learning_navigator.dto.request.StudentRequest;
import com.crio.learning_navigator.dto.response.StudentResponse;
import com.crio.learning_navigator.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { SubjectMapper.class, ExamMapper.class })
public interface StudentMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "enrolledSubjects", ignore = true)
  @Mapping(target = "enrolledExams", ignore = true)
  Student toEntity(StudentRequest request);

  StudentResponse toResponse(Student student);
}