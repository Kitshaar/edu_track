package com.kitshaar.edu_track.school.controllers;


import com.kitshaar.edu_track.school.Dto.attendances.AttendanceDto;
import com.kitshaar.edu_track.school.Dto.attendances.GetAttendanceDto;
import com.kitshaar.edu_track.school.Dto.students.GetStudentTableDto;
import com.kitshaar.edu_track.school.Dto.students.StudentTableDto;
import com.kitshaar.edu_track.school.services.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("school")
@RestController
public class AttendanceController {

    @Autowired
    private AttendanceService service;

    @GetMapping("attendances")
    public ResponseEntity<List<GetAttendanceDto>> getAllAttendances()
    {
        return service.getAllAttendances();
    }

    @GetMapping("attendances/{id}")
    public ResponseEntity<GetAttendanceDto> getAttendance(@PathVariable Long id)
    {
        return service.getAttendance(id);
    }

    @PostMapping("attendances")
    public ResponseEntity<String> addAttendance(@Valid @RequestBody AttendanceDto attendanceDto)
    {
        return service.addAttendance(attendanceDto);
    }

    @PutMapping("attendances/{id}")
    public ResponseEntity<String> updateAttendance(@PathVariable Long id, @RequestBody AttendanceDto attendanceDto)
    {
        return service.updateAttendance(id, attendanceDto);
    }

    @DeleteMapping("attendances/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable Long id)
    {
        return service.deleteAttendance(id);
    }
}
