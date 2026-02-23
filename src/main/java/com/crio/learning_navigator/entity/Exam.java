package com.crio.learning_navigator.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String examName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subject_id", nullable = false)
  private Subject subject;

  @ManyToMany(mappedBy = "enrolledExams", fetch = FetchType.LAZY)
  @Builder.Default
  private List<Student> enrolledStudents = new ArrayList<>();
}