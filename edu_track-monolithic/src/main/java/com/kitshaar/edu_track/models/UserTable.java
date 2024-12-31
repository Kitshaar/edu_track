package com.kitshaar.edu_track.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

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

	public UserTable() {

	}

	public UserTable(Integer id, String name, String email, String password, String hash_password,
			OffsetDateTime createdAt, LocalDate updatedAt, String role) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.hashPassword = hash_password;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashPassword(String hash_password) {
		this.hashPassword = hash_password;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

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
