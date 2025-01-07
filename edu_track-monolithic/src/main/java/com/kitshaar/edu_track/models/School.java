package com.kitshaar.edu_track.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "school")
public class School {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	
	@Column(name = "school_id", nullable=false)
	private Integer schoolId; 
	
	@Column(name = "school_name", nullable=false)
	private String schoolName; 
	
	@Column(nullable=false)
	private String address; 
	
	@Column(name = "phone_no", nullable=false)
	private String phoneNo;

	@Override
	public String toString() {
		return "School [id=" + id + ", schoolId=" + schoolId + ", schoolName=" + schoolName + ", address=" + address
				+ ", phoneNo=" + phoneNo + "]";
	} 
	
}
