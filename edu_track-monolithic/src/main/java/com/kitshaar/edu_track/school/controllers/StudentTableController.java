package com.kitshaar.edu_track.school.controllers;

import com.kitshaar.edu_track.school.Dto.students.GetStudentTableDto;
import com.kitshaar.edu_track.school.Dto.students.StudentTableDto;
import com.kitshaar.edu_track.school.services.StudentTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("school")
@RestController
public class StudentTableController {

    @Autowired
    private StudentTableService service;

    @GetMapping("students")
    public ResponseEntity<List<GetStudentTableDto>> getAllStudents()
    {
        return service.getAllStudents();
    }

    @GetMapping("students/{id}")
    public ResponseEntity<GetStudentTableDto> getStudent(@PathVariable Long id)
    {
        return service.getStudent(id);
    }

    @PostMapping("students")
    public ResponseEntity<String> addStudent(@Valid @RequestBody StudentTableDto studentTableDto)
    {
        return service.addStudent(studentTableDto);
    }

    @PutMapping("students/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody StudentTableDto studentTableDto)
    {
        return service.updateStudent(id, studentTableDto);
    }

    @DeleteMapping("students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id)
    {
        return service.deleteStudent(id);
    }


}
