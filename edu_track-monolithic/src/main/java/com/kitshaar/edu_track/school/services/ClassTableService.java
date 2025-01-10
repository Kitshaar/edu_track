package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.Dto.ClassTableDto;
import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.repositories.ClassTableRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class ClassTableService {

    @Autowired
    private ClassTableRepo classTableRepo;

    private static final Logger logger = LoggerFactory.getLogger(ClassTableService.class);

    public ResponseEntity<List<ClassTableDto>> getAllClasses() {

        try {
            List<ClassTable> classTables = classTableRepo.findAll();
            // Map ClassTable entities to DTOs
            List<ClassTableDto> classTableDtos = classTables.stream()
                    .map(classTable -> new ClassTableDto(classTable.getClassId(), classTable.getClassName()))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(classTableDtos, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error with proper details
            logger.error("Error occurred while fetching classes: {}", e.getMessage(), e);
            // Return a bad request response in case of error
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ClassTableDto> getClassById(Long id) {
        try {
            // Find class by ID
            return classTableRepo.findById(id)
                    .map(classValue -> {
                        // Convert the ClassTable entity to ClassTableDto
                        ClassTableDto classTableDto = new ClassTableDto(classValue.getClassId(), classValue.getClassName());
                        return new ResponseEntity<>(classTableDto, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        // Log the error when the class is not found
                        logger.error("Class with ID {} not found.", id);
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    });
        } catch (Exception e) {
            logger.error("Unexpected error while fetching class with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> addClass(ClassTableDto classValue) {
        if (classValue == null || classValue.getClassName() == null || classValue.getClassName().isEmpty()) {
            return new ResponseEntity<>("Invalid class details: class value or name is missing", HttpStatus.BAD_REQUEST);
        }
        try {
            ClassTable class1 = ClassTable.builder()
                            .className(classValue.getClassName())
                            .build();

            classTableRepo.save(class1);
            return new ResponseEntity<>("Class added successfully", HttpStatus.CREATED);
        }catch (OptimisticLockingFailureException e) {
            // Handle OptimisticLockingFailureException
            logger.error("Optimistic locking failure while adding class: {}", e.getMessage(), e);
            return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
                    HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Unexpected error while adding class: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error while adding class", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> updateUser(Long id, ClassTableDto classTable) {
        if (id == null || id < 1L) {
            logger.error("Invalid ID: {}", id);
            return new ResponseEntity<>("Invalid ID provided", HttpStatus.BAD_REQUEST);
        }
        if (classTable == null || classTable.getClassName() == null || classTable.getClassName().isEmpty()) {
            logger.error("Invalid class details provided for ID: {}", id);
            return new ResponseEntity<>("Invalid class details provided", HttpStatus.BAD_REQUEST);
        }
        try {
            // Check if the class exists
            ClassTable existingClass = classTableRepo.findById(id).orElse(null);
            if (existingClass == null) {
                return new ResponseEntity<>("Class not found", HttpStatus.NOT_FOUND);
            }

            // Update the fields, including class name
            existingClass.setClassName(classTable.getClassName());

            // Save the updated entity
            classTableRepo.save(existingClass);
            return new ResponseEntity<>("Class updated successfully", HttpStatus.OK);

        } catch (OptimisticLockingFailureException e) {
            logger.error("Optimistic locking conflict while updating class with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch", HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Error while updating class with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Error while updating class", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> deleteClass(Long id) {
        if(id == null || id < 1L)
        {
            logger.error("Invalid ID: {}", id);
            return new ResponseEntity<>("Invalid ID provided", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!classTableRepo.existsById(id)) {
                return new ResponseEntity<>("Class not found", HttpStatus.NOT_FOUND);
            }
            classTableRepo.deleteById(id);
            return new ResponseEntity<>("Class deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while deleting class with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Error while deleting class", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
