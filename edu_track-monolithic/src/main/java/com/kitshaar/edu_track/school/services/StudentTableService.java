package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.Dto.students.GetStudentTableDto;
import com.kitshaar.edu_track.school.Dto.students.StudentTableDto;
import com.kitshaar.edu_track.school.mappers.GetMapping;
import com.kitshaar.edu_track.school.mappers.InsertMapping;
import com.kitshaar.edu_track.school.models.ClassTable;

import com.kitshaar.edu_track.school.models.ParentTable;
import com.kitshaar.edu_track.school.models.StudentTable;
import com.kitshaar.edu_track.school.repositories.ClassTableRepo;
import com.kitshaar.edu_track.school.repositories.ParentTableRepo;
import com.kitshaar.edu_track.school.repositories.StudentTableRepo;
import jakarta.validation.Valid;
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
import java.util.Optional;

@Service
public class StudentTableService {

    @Autowired
    private StudentTableRepo studentTableRepo;
    @Autowired
    private ClassTableRepo classTableRepo;
    @Autowired
    private ParentTableRepo parentTableRepo;

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);



    @Transactional(transactionManager = "schoolTransactionManager", readOnly = true)
    public ResponseEntity<List<GetStudentTableDto>> getAllStudents() {
        try {
            List<GetStudentTableDto> students = studentTableRepo.findAllStudentDetails();

            return new ResponseEntity<>(students, HttpStatus.OK);

        } catch (Exception e) {
            // Log the error with proper details
            logger.error("Error occurred while fetching students: {}", e.getMessage(), e);
            // Return a bad request response in case of error
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager", readOnly = true)
    public ResponseEntity<GetStudentTableDto> getStudent(Long id) {
        try {

            // Find student by ID
            return studentTableRepo.findById(id)
                    .map(student -> {
                        // Convert the Student entity to GetStudentDto
                        GetStudentTableDto getStudentTableDto = GetMapping.mapToStudentTableDto(student);
                        return ResponseEntity.ok(getStudentTableDto);
                    })
                    .orElseGet(() -> {
                        // Log the error when the student is not found
                        logger.warn("Student with ID {} not found.", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    });
        } catch (Exception e) {
            // Log unexpected errors with proper context
            logger.error("Unexpected error while fetching student with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> addStudent(@Valid StudentTableDto studentTableDto) {

        try {
            // Fetch the ClassTable entity by ID
            ClassTable classTable = classTableRepo.findById(studentTableDto.getClassId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid class ID: " + studentTableDto.getClassId()));

            // Fetch the ParentTable entity by ID
            ParentTable parentTable = parentTableRepo.findById(studentTableDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent ID: " + studentTableDto.getParentId()));

            // Map StudentDto to Student entity
            StudentTable student = InsertMapping.maptoStudent(studentTableDto, classTable, parentTable);

            studentTableRepo.save(student);

            return new ResponseEntity<>("Student successfully added", HttpStatus.CREATED);
        } catch (OptimisticLockingFailureException e) {
            // Handle OptimisticLockingFailureException
            logger.error("Optimistic locking failure while adding student: {}", e.getMessage(), e);
            return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
                    HttpStatus.CONFLICT);

        } catch (IllegalArgumentException e) {
            // Handle invalid class or parent ID
            logger.error("Invalid class or parent ID provided: {}", e.getMessage(), e);
            return new ResponseEntity<>("Invalid class or parent ID: " + e.getMessage(), HttpStatus.BAD_REQUEST);

        }
        catch (Exception e) {
            // Handle unexpected exceptions
            logger.error("Unexpected error while adding student: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error while adding student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> updateStudent(Long id, StudentTableDto studentTableDto) {
        if (id == null || id < 1L) {
            logger.error("Invalid ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid ID provided");
        }
        if (studentTableDto == null) {
            logger.error("Invalid student details provided for ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid student details provided");
        }
        try {
            StudentTable existingStudent = studentTableRepo.findById(id).orElse(null);
            if (existingStudent == null)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Student record with ID " + id + " not found");
            }
            //Validate and set ClassTable
            if (studentTableDto.getClassId() != null && studentTableDto.getClassId() > 0)
            {
                ClassTable classTable = classTableRepo.findById(studentTableDto.getClassId())
                        .orElseThrow(() -> new
                                IllegalArgumentException("Invalid class ID: " + studentTableDto.getClassId() + " Provided.")
                        );
                existingStudent.setClassTable(classTable);
            }

            //Validate and set ParentTable
            if (studentTableDto.getParentId() != null && studentTableDto.getParentId() > 0)
            {
                ParentTable parent = parentTableRepo.findById(studentTableDto.getParentId())
                        .orElseThrow(() -> new
                                IllegalArgumentException("Invalid parent ID: " + studentTableDto.getParentId() + " Provided.")
                        );
                existingStudent.setParent(parent);
            }
            //Update the rest of fields in Student
            Optional.ofNullable(studentTableDto.getName()).ifPresent(existingStudent::setName);

            studentTableRepo.save(existingStudent);
            return ResponseEntity.ok("Student record updated successfully");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input while updating register with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (OptimisticLockingFailureException e) {
            logger.error("Optimistic locking conflict while updating register with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Entity version mismatch");
        } catch (Exception e) {
            logger.error("Error while updating register with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the register");
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> deleteStudent(Long id) {
        if(id == null || id < 1L)
        {
            logger.error("Invalid ID: {}", id);
            return new ResponseEntity<>("Invalid ID provided", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!studentTableRepo.existsById(id))
            {
                return new ResponseEntity<>("student record not found", HttpStatus.NOT_FOUND);
            }
            studentTableRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Student record deleted Successfully");
        } catch (Exception e) {
            logger.error("Error while deleting student record with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Error while deleting student record", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
