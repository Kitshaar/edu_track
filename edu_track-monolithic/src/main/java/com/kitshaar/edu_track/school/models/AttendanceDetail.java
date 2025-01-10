package com.kitshaar.edu_track.school.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "attendance_detail",
        indexes = @Index(name = "idx_attendance_student",
                columnList = "attendance_id, student_id"))
public class AttendanceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attendance attendance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentTable student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;
}
