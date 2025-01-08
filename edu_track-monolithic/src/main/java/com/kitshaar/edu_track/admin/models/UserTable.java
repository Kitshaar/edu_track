package com.kitshaar.edu_track.admin.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_table") // Maps to the exact table name
public class UserTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(name = "hash_password", nullable = false)
	private String hashPassword;

	@Column(name = "created_at", updatable = false)
	private OffsetDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "updated_at")
	private LocalDate updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(nullable = false)
	private String role;

	@Override
	public String toString() {
		return "UserTable [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", hash_password=" + hashPassword + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", role=" + role + "]";
	}

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
