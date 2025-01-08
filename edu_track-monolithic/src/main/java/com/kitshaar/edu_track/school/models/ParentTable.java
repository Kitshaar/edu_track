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
@Table(name = "parent_table")
public class ParentTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id", nullable = false)
    private Long parentId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "phone", nullable = false, length = 10)
    private String phone;
    @Column(name = "alt_phone", length = 10)
    private String altPhone;
    @Column(name = "email")
    private String email;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true) // One parent to many students
    private List<StudentTable> students;
}
