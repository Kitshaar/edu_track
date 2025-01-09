package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.repositories.AttendanceDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceDetailService {

    @Autowired
    private AttendanceDetailRepo attendanceDetailRepo;
}
