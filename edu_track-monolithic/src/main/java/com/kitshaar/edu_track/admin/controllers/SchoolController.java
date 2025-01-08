package com.kitshaar.edu_track.admin.controllers;

import com.kitshaar.edu_track.admin.models.School;
import com.kitshaar.edu_track.admin.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class SchoolController {
	
	@Autowired
    SchoolService service;
	
	
//	GET /schools - Get all schools.
	@GetMapping("schools")
	public ResponseEntity<List<School>> getAllSchools()
	{
		return service.getAllSchools(); 
	}
//	GET /schools/{id} - Get school by ID.
	@GetMapping("schools/{id}")
	public ResponseEntity<School> getSchool(@PathVariable Integer id)
	{
		return service.getSchool(id);
	}
	
//	POST /schools - Create a new school.
	@PostMapping("schools")
	public ResponseEntity<String> addSchool(@RequestBody School school)
	{
		return service.addSchool(school); 
	}
//	PUT /schools/{id} - Update an existing school.
	@PutMapping("schools/{id}")
	public ResponseEntity<String> updateSchool(@PathVariable Integer id, @RequestBody School school)
	{
		return service.updateSchool(id, school);
	}
	
//	DELETE /schools/{id} - Delete a school.
	@DeleteMapping("schools/{id}")
	public ResponseEntity<String> deleteSchool(@PathVariable Integer id)
	{
		return service.deleteSchool(id);
	}

}
