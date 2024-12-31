package com.kitshaar.edu_track.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kitshaar.edu_track.models.UserTable;
import com.kitshaar.edu_track.service.UserTableService;

@RestController
@RequestMapping("admin")
public class UserTableController {

	@Autowired
	UserTableService service; 
	
//	GET /users - Get all users.
	@GetMapping("users")
	public ResponseEntity<List<UserTable>> getAllUsers()
	{
		return service.getAllUsers(); 
	}
	
//	GET /users/{id} - Get user by ID.
	@GetMapping("users/{id}")
	public ResponseEntity<UserTable> getUser(@PathVariable Integer id)
	{
		return service.getUser(id); 
	}
	
//	POST /users - Create a new user.
	@PostMapping("users")
	public ResponseEntity<String> addUser(@RequestBody UserTable user)
	{
		System.out.println("In controller");
		return service.addUser(user); 
	}
	
//	PUT /users/{id} - Update an existing user.
//	@PutMapping("users/{id}")
	
	
	
//	DELETE /users/{id} - Delete a user.
	@DeleteMapping("users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer id)
	{
		return service.deleteUser(id);
	}
	
}
