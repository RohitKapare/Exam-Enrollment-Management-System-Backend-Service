package com.crio.learning_navigator.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String subjectName;

  @ManyToMany(mappedBy = "enrolledSubjects", fetch = FetchType.LAZY)
  @Builder.Default
  private List<Student> enrolledStudents = new ArrayList<>();
}