package com.kitshaar.edu_track.models;

import java.time.LocalDate;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
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
	
	@Column(name = "created_at", nullable=true)
	private OffsetDateTime createdAt; 
	
	@Column(nullable=false)
	private Integer amount; 
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "due_date", nullable=true, columnDefinition = "DATE")
	private LocalDate dueDate; 
	
	@Column(nullable=false)
	private String status; 

}
