package com.kitshaar.edu_track.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kitshaar.edu_track.models.UserTable;
import com.kitshaar.edu_track.repositories.UserTableRepo;

@Service
public class UserTableService {

	@Autowired
	UserTableRepo repo;

	public ResponseEntity<List<UserTable>> getAllUsers() {
		try {
			return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<UserTable> getUser(Integer id) {
		try {
			return new ResponseEntity<>(repo.findById(id).get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new UserTable(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> addUser(UserTable user) {
		if (user == null) {
			return new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST);
		}
		try {
			
			System.out.println("In try");
			
			repo.save(user); // Try to save the user
			System.out.println("used save");
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

	        return new ResponseEntity<>("Success", HttpStatus.NO_CONTENT); 
		} catch (Exception e) {
			// Catch any other exceptions that might occur
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
