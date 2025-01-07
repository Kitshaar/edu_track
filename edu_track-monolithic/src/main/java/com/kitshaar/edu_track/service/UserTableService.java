package com.kitshaar.edu_track.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kitshaar.edu_track.models.UserTable;
import com.kitshaar.edu_track.repositories.UserTableRepo;

@Service
public class UserTableService {

	@Autowired
	UserTableRepo repo;

	@Transactional
	public ResponseEntity<List<UserTable>> getAllUsers() {
		try {
			return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	@Transactional
	public ResponseEntity<UserTable> getUser(Integer id) {
		try {
			return new ResponseEntity<>(repo.findById(id).get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new UserTable(), HttpStatus.BAD_REQUEST);
	}

	@Transactional
	public ResponseEntity<String> addUser(UserTable user) {
		if (user == null) {
			return new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}
		try {

			repo.save(user); // Try to save the user
			System.out.println("Created Successfully");
			return new ResponseEntity<>("Success", HttpStatus.CREATED);

		} catch (OptimisticLockingFailureException e) {
			// Handle OptimisticLockingFailureException
			System.out.println("Locking Failure");
			return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
					HttpStatus.CONFLICT);
		} catch (Exception e) {
			// Catch any other exceptions that might occur
			e.printStackTrace();
			System.out.println("Exception");
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseEntity<String> deleteUser(Integer id) {
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

	@Transactional
	public ResponseEntity<String> updateUser(Integer id, UserTable user) {
		if (id == null || !repo.existsById(id)) {
			return new ResponseEntity<>("Id being sent is null or doesn't exist. It is not allowed",
					HttpStatus.BAD_REQUEST);
		}
		if (user == null) {
			return new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}
		try {
			UserTable oldEntry = repo.findById(id)
				    .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist"));

			  // Update fields
			oldEntry.setUpdatedAt(LocalDate.now());
	        if (user.getName() != null) oldEntry.setName(user.getName());
	        if (user.getEmail() != null) oldEntry.setEmail(user.getEmail());
	        if (user.getRole() != null) oldEntry.setRole(user.getRole());
	        if (user.getHashPassword() != null) oldEntry.setHashPassword(user.getHashPassword());
	        if (user.getPassword() != null) oldEntry.setPassword(user.getPassword());

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



}
