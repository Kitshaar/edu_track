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
@Table(name="payment")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	
	@Column(name = "payment_id", nullable=false)
	private Integer paymentId; 
	
	@Column(name = "school_id", nullable=false)
	private Integer schoolId;
	
	@Column(name = "created_at", updatable=false)
	private OffsetDateTime createdAt; 
	
	@Column(nullable=false)
	private Integer amount; 
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "due_date", nullable=true, columnDefinition = "DATE")
	private LocalDate dueDate; 
	
	@Column(nullable=false)
	private String status;

	@Override
	public String toString() {
		return "Payment [id=" + id + ", paymentId=" + paymentId + ", schoolId=" + schoolId + ", createdAt=" + createdAt
				+ ", amount=" + amount + ", dueDate=" + dueDate + ", status=" + status + "]";
	}

	@PrePersist
	public void onCreate() {
		if (createdAt == null) {
			createdAt = OffsetDateTime.now(); // If not provided, set to current time.
		}
	}

}
