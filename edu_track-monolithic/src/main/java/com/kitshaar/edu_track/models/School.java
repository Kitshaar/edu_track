package com.kitshaar.edu_track.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

	public School(Integer id, Integer schoolId, String schoolName, String address, String phoneNo) {
	
		this.id = id;
		this.schoolId = schoolId;
		this.schoolName = schoolName;
		this.address = address;
		this.phoneNo = phoneNo;
	}

	public School() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Override
	public String toString() {
		return "School [id=" + id + ", schoolId=" + schoolId + ", schoolName=" + schoolName + ", address=" + address
				+ ", phoneNo=" + phoneNo + "]";
	} 
	
}
