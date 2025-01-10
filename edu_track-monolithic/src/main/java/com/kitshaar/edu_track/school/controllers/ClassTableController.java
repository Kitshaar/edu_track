package com.kitshaar.edu_track.school.controllers;


import com.kitshaar.edu_track.school.Dto.ClassTableDto;
import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.services.ClassTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("school")
@RestController
public class ClassTableController {

    @Autowired
    private ClassTableService service;

    @GetMapping("classes")
    public ResponseEntity<List<ClassTableDto>> getAllClasses()
    {
        return service.getAllClasses();
    }

    @GetMapping("classes/{id}")
    public ResponseEntity<ClassTableDto> getClass(@PathVariable Long id)
    {
        return  service.getClassById(id);
    }

    //	POST school/classes - Create a new class.
    @PostMapping("classes")
    public ResponseEntity<String> addClass(@RequestBody ClassTableDto classValue) {
        return service.addClass(classValue);
    }

    //	PUT school/classes/{id} - Update an existing class.
    @PutMapping("classes/{id}")
    public ResponseEntity<String> updateClass(@PathVariable Long id, @RequestBody ClassTableDto classTable) {
        return service.updateUser(id, classTable);
    }

    @DeleteMapping("classes/{id}")
    public ResponseEntity<String> deleteClassById(@PathVariable Long id) { return service.deleteClass(id);}

}
