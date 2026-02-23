package com.crio.learning_navigator.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "student_subject_enrollment", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
  @Builder.Default
  private List<Subject> enrolledSubjects = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "student_exam_registration", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "exam_id"))
  @Builder.Default
  private List<Exam> enrolledExams = new ArrayList<>();
}