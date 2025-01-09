package com.kitshaar.edu_track.school.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "register",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"phone"}),       // Ensures unique phone numbers
                @UniqueConstraint(columnNames = {"alt_phone"}),   // Ensures unique alternate phone numbers
                @UniqueConstraint(columnNames = {"email"})        // Ensures unique emails
        },
        indexes = {
                @Index(name = "idx_register_class", columnList = "class_id"),  // Index on class_id for quick lookups
                @Index(name = "idx_register_phone", columnList = "phone"),    // Index on phone for fast searches
                @Index(name = "idx_register_email", columnList = "email")     // Index on email for fast searches
        })
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "f_name", nullable = false)
    private String fName;
    @Column(name = "m_name", nullable = false)
    private String mName;
    @Column(name = "phone", nullable = false, length = 10)
    private String phone;
    @Column(name = "alt_phone", length = 10)
    private String altPhone;
    @Column(name = "email")
    private String email;
    @Column(name = "address", nullable = false)
    private String address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false) // Foreign Key to ClassTable
    private ClassTable classTable;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "updated_at")
    private LocalDate updatedAt;


    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now(); // If not provided, set to current time.
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDate.now(); // Automatically update on modification.
    }


}
