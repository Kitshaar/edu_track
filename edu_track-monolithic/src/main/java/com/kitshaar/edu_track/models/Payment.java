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
import jakarta.persistence.Table;

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
	
	public Payment(Integer id, Integer paymentId, Integer schoolId, OffsetDateTime createdAt, Integer amount,
			LocalDate dueDate, String status) {
		super();
		this.id = id;
		this.paymentId = paymentId;
		this.schoolId = schoolId;
		this.createdAt = createdAt;
		this.amount = amount;
		this.dueDate = dueDate;
		this.status = status;
	}

	public Payment() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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
