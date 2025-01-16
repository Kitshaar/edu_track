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
@Table(name = "student_table",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "class_id"}) // Ensures unique student names within a class
        },
        indexes = {
                @Index(name = "idx_student_class", columnList = "class_id"), // Index on class_id for faster lookups
                @Index(name = "idx_student_parent", columnList = "parent_id") // Index on parent_id
        })
public class StudentTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", nullable = false, foreignKey = @ForeignKey(name = "FK_student_parent"))
    private ParentTable parent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id", nullable = false, foreignKey = @ForeignKey(name = "FK_student_class"))
    private ClassTable classTable;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceDetail> attendances;

    @Version
    @Column(name = "version")
    private Long version;
}
