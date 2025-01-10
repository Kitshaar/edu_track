package com.kitshaar.edu_track.school.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "class_table",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"class_name"}) // Ensures unique class names
        },
        indexes = {
                @Index(name = "idx_class_name", columnList = "class_name") // Index on class_name for faster lookups
        })
public class ClassTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id", nullable = false)
    private Long classId;
    @Column(name = "class_name", nullable = false)
    private String className;
    @OneToMany(mappedBy = "classTable", cascade = CascadeType.ALL, orphanRemoval = true) // One class to many students
    private List<StudentTable> students;
    @OneToMany(mappedBy = "classTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances ;
    @OneToMany(mappedBy = "classTable", cascade = CascadeType.ALL, orphanRemoval = true) // One class to many registers
    private List<Register> registers ;

    @Version
    @Column(name = "version")
    private Long version;
}
