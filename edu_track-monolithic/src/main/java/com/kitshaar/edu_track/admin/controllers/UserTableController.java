package com.kitshaar.edu_track.admin.controllers;

import com.kitshaar.edu_track.admin.models.UserTable;
import com.kitshaar.edu_track.admin.service.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class UserTableController {

    @Autowired
    UserTableService service;

    //	GET /users - Get all users.
    @GetMapping("users")
    public ResponseEntity<List<UserTable>> getAllUsers() {
        return service.getAllUsers();
    }

    //	GET /users/{id} - Get user by ID.
    @GetMapping("users/{id}")
    public ResponseEntity<UserTable> getUser(@PathVariable Integer id) {
        return service.getUser(id);
    }

    //	POST /users - Create a new user.
    @PostMapping("users")
    public ResponseEntity<String> addUser(@RequestBody UserTable user) {
		return service.addUser(user);
    }

    //	PUT /users/{id} - Update an existing user.
    @PutMapping("users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody UserTable user) {
        return service.updateUser(id, user);
    }


    //	DELETE /users/{id} - Delete a user.
    @DeleteMapping("users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        return service.deleteUser(id);
    }

}
