package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.repositories.StudentTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentTableService {

    @Autowired
    private StudentTableRepo studentTableRepo;

}
