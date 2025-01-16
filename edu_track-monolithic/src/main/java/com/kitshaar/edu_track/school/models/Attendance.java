package com.kitshaar.edu_track.school.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"class_id", "date"}) // Ensures one attendance record per class per date
        },
        indexes = {
                @Index(name = "idx_attendance_class_date", columnList = "class_id, date") // Index on class_id and date
        })
public class Attendance {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long attendanceId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "class_id", nullable = false)
        private ClassTable classTable;


        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @Column(nullable = false)
        private LocalDate date;

        @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<AttendanceDetail> attendanceDetails = new ArrayList<>();


        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Version
        @Column(name = "version")
        private Long version;

        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
            this.updatedAt = LocalDateTime.now();
        }

    }
