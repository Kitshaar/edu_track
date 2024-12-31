package com.kitshaar.edu_track.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kitshaar.edu_track.models.School;
import com.kitshaar.edu_track.repositories.SchoolRepo;

@Service
public class SchoolService {

	@Autowired
	SchoolRepo repo;

	@Transactional
	public ResponseEntity<List<School>> getAllSchools() {
		try {
			return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	@Transactional
	public ResponseEntity<School> getSchool(Integer id) {
		try {
			return new ResponseEntity<>(repo.findById(id).get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new School(), HttpStatus.BAD_REQUEST);
	}

	@Transactional
	public ResponseEntity<String> addSchool(School school) {
		if (school == null) {
			return new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}
		if (school.getPhoneNo().length() != 10)
		{
			return new ResponseEntity<>("Phone number must be of 10 characters", HttpStatus.BAD_REQUEST);
		}
		if (!school.getPhoneNo().matches("\\d{10}")) {
		    return new ResponseEntity<>("Phone number must contain only numeric characters and be of 10 digits", HttpStatus.BAD_REQUEST);
		}
		try {
			repo.save(school); // Try to save the user
			return new ResponseEntity<>("Success", HttpStatus.CREATED);
		} catch (OptimisticLockingFailureException e) {
			// Handle OptimisticLockingFailureException
			return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
					HttpStatus.CONFLICT);
		} catch (Exception e) {
			// Catch any other exceptions that might occur
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseEntity<String> updateSchool(Integer id, School school) {
		if (id == null || !repo.existsById(id)) {
			return new ResponseEntity<>("Id being sent is null or doesn't exist. It is not allowed",
					HttpStatus.BAD_REQUEST);
		}
		if (school == null) {
			return new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}
		if (school.getPhoneNo().length() != 10)
		{
			return new ResponseEntity<>("Phone number must be of 10 characters", HttpStatus.BAD_REQUEST);
		}
		if (!school.getPhoneNo().matches("\\d{10}")) {
		    return new ResponseEntity<>("Phone number must contain only numeric characters and be of 10 digits", HttpStatus.BAD_REQUEST);
		}
		try {
			School oldEntry = repo.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist"));

			if (school.getSchoolId() != null)
				oldEntry.setSchoolId(school.getSchoolId());
			if (school.getSchoolName() != null)
				oldEntry.setSchoolName(school.getSchoolName());
			if (school.getAddress() != null)
				oldEntry.setAddress(school.getAddress());
			if (school.getPhoneNo() != null)
				oldEntry.setPhoneNo(school.getPhoneNo());

			// Save the updated entity
			repo.save(oldEntry);
			return new ResponseEntity<>("User updated successfully.", HttpStatus.OK);

		} catch (OptimisticLockingFailureException e) {
			// Handle OptimisticLockingFailureException
			return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
					HttpStatus.CONFLICT);
		} catch (Exception e) {
			// Catch any other exceptions that might occur
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	public ResponseEntity<String> deleteSchool(Integer id) {
		if (id == null) {
			return new ResponseEntity<>("Id being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}

		try {
			// Check if the user exists first
			if (!repo.existsById(id)) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			}

			// Try to delete the user
			repo.deleteById(id);

			return new ResponseEntity<>("Successfull deletion", HttpStatus.OK);
		} catch (Exception e) {
			// Catch any other exceptions that might occur
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
