package com.kitshaar.edu_track.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
//@Table(name = "user_table") // Maps to the exact table name
public class User_table {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	
//	@Column(nullable=true)
	private String name;
	
//	@Column(name="email", nullable=true)
	private String email;
	
//	@Column(nullable=true)
	private String password;
	
//	@Column(name = "hash_password")
    private String hash_password;
	
//    @Column(name = "created_at")
    private OffsetDateTime created_at;

//    @Column(name = "updated_at")
    private LocalDate updated_at;

//	@Column(nullable=true)
    private String role;
	
//	@PrePersist
//    public void onCreate() {
//        if (createdAt == null) {
//            createdAt = OffsetDateTime.now(); // If not provided, set to current time.
//        }
//    }

//    @PreUpdate
//    public void onUpdate() {
//        updatedAt = LocalDate.now(); // Automatically update on modification.
//    }

}
