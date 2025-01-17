package com.kitshaar.edu_track.school.controllers;

import com.kitshaar.edu_track.school.Dto.attendanceDetails.AttendanceDetailDto;
import com.kitshaar.edu_track.school.Dto.attendanceDetails.GetAttendanceDetailDto;

import com.kitshaar.edu_track.school.services.AttendanceDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("school")
@RestController
public class AttendanceDetailController {

    @Autowired
    private AttendanceDetailService service;

    @GetMapping("attendanceDetails")
    public ResponseEntity<List<GetAttendanceDetailDto>> getAllAttendanceDetails()
    {
        return service.getAllAttendanceDetails();
    }

    @GetMapping("attendanceDetails/{id}")
    public ResponseEntity<GetAttendanceDetailDto> getAttendanceDetail(@PathVariable Long id)
    {
        return service.getAttendanceDetail(id);
    }

    @PostMapping("attendanceDetails")
    public ResponseEntity<String> addAttendanceDetail(@Valid @RequestBody AttendanceDetailDto attendanceDetailDto)
    {
        return service.addAttendanceDetail(attendanceDetailDto);
    }

    @PutMapping("attendanceDetails/{id}")
    public ResponseEntity<String> updateAttendanceDetail(@PathVariable Long id, @RequestBody AttendanceDetailDto attendanceDetailDto)
    {
        return service.updateAttendanceDetail(id, attendanceDetailDto);
    }

    @DeleteMapping("attendanceDetails/{id}")
    public ResponseEntity<String> deleteAttendanceDetail(@PathVariable Long id)
    {
        return service.deleteAttendanceDetail(id);
    }
}
