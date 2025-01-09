package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.repositories.ClassTableRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ClassTableService {

    @Autowired
    private ClassTableRepo classTableRepo;

    private static final Logger logger = LoggerFactory.getLogger(ClassTableService.class);

    public ResponseEntity<List<ClassTable>> getAllClasses() {

        try {
            List<ClassTable> classTables = classTableRepo.findAll();
            return new ResponseEntity<>(classTables, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error with proper details
            logger.error("Error occurred while fetching classes: {}", e.getMessage(), e);
            // Return a bad request response in case of error
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ClassTable> getClassById(Long id) {
        try {
            return classTableRepo.findById(id)
                    .map(classValue -> new ResponseEntity<>(classValue, HttpStatus.OK))
                    .orElseGet(() -> {
                        logger.error("Class with ID {} not found.", id);
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    });
        } catch (Exception e) {
            logger.error("Unexpected error while fetching class with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
