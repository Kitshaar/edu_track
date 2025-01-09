package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.repositories.RegisterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private RegisterRepo registerRepo;
}
