package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.repositories.ParentTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentTableService {

    @Autowired
    private ParentTableRepo parentTableRepo;

}
