package com.kitshaar.edu_track.school.controllers;

import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.services.ClassTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("school")
@RestController
public class ClassTableController {

    @Autowired
    private ClassTableService service;

    @GetMapping("classes")
    public ResponseEntity<List<ClassTable>> getAllClasses()
    {
        return service.getAllClasses();
    }

    @GetMapping("classes/{id}")
    public ResponseEntity<ClassTable> getClass(@PathVariable Long id)
    {
        return  service.getClassById(id);
    }

}
