package com.kitshaar.edu_track.school.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "class_table")
public class ClassTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id", nullable = false)
    private Long classId;
    @Column(name = "class_name", nullable = false)
    private String className;
    @OneToMany(mappedBy = "classTable", cascade = CascadeType.ALL, orphanRemoval = true) // One class to many students
    private List<StudentTable> students;
}
