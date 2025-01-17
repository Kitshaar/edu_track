package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.Dto.attendances.AttendanceDto;
import com.kitshaar.edu_track.school.Dto.attendances.GetAttendanceDto;
import com.kitshaar.edu_track.school.mappers.GetMapping;
import com.kitshaar.edu_track.school.mappers.InsertMapping;
import com.kitshaar.edu_track.school.models.Attendance;
import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.repositories.AttendanceRepo;
import com.kitshaar.edu_track.school.repositories.ClassTableRepo;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepo attendanceRepo;
    @Autowired
    private ClassTableRepo classTableRepo;
    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);


    @Transactional(transactionManager = "schoolTransactionManager", readOnly = true)
    public ResponseEntity<List<GetAttendanceDto>> getAllAttendances() {
        try {
                List<Attendance> attendances = attendanceRepo.findAllWithClassTable();
                List<GetAttendanceDto> attendanceDtoList = attendances.stream()
                        .map(GetMapping::mapToAttendanceDto)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(attendanceDtoList, HttpStatus.OK);
        }
        catch (Exception e) {
            // Log the error with proper details
            logger.error("Error occurred while fetching attendances: {}", e.getMessage(), e);
            // Return a bad request response in case of error
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager", readOnly = true)
    public ResponseEntity<GetAttendanceDto> getAttendance(Long id) {
        try {
            return attendanceRepo.findById(id)
                    .map(attendance -> {
                        GetAttendanceDto attendanceDto = GetMapping.mapToAttendanceDto(attendance);
                        return ResponseEntity.ok(attendanceDto);
                    })
                    .orElseGet(() -> {
                        // Log the error when the attendance is not found
                        logger.warn("Attendance with ID {} not found.", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    });
        } catch (Exception e) {
            // Log unexpected errors with proper context
            logger.error("Unexpected error while fetching attendance with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> addAttendance(@Valid AttendanceDto attendanceDto) {
        try {

            // Fetch the ClassTable entity by ID
            ClassTable classTable = classTableRepo.findById(attendanceDto.getClassId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid class ID: " + attendanceDto.getClassId()));

            Attendance attendance = InsertMapping.mapToAttendance(attendanceDto, classTable);

            attendanceRepo.save(attendance);
            return ResponseEntity.status(HttpStatus.CREATED).body("Attendance added successfully.");
        } catch (OptimisticLockingFailureException e) {
            // Handle OptimisticLockingFailureException
            logger.error("Optimistic locking failure while adding attendance: {}", e.getMessage(), e);
            return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
                    HttpStatus.CONFLICT);

        } catch (IllegalArgumentException e) {
            // Handle invalid attendance ID
            logger.error("Invalid attendance ID provided: {}", e.getMessage(), e);
            return new ResponseEntity<>("Invalid attendance ID: " + e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            // Handle unexpected exceptions
            logger.error("Unexpected error while adding attendance: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error while adding attendance", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> updateAttendance(Long id, AttendanceDto attendanceDto) {
        if (id == null || id < 1L) {
            logger.error("Invalid ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid ID provided");
        }
        if (attendanceDto == null) {
            logger.error("Invalid Attendance details provided for ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid Attendance details provided");
        }
        try {
            Attendance existingAttendance = attendanceRepo.findById(id).orElse(null);

            if (existingAttendance == null)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Attendance record with ID " + id + " not found");
            }

            //Validate and set ClassTable
            if (attendanceDto.getClassId() != null && attendanceDto.getClassId() > 0)
            {
                ClassTable classTable = classTableRepo.findById(attendanceDto.getClassId())
                        .orElseThrow(() -> new
                                IllegalArgumentException("Invalid class ID: " + attendanceDto.getClassId() + " Provided.")
                        );
                existingAttendance.setClassTable(classTable);
            }
            //Update the rest of fields in Student
            existingAttendance.setUpdatedAt(LocalDateTime.now());
            Optional.ofNullable(attendanceDto.getDate()).ifPresent(existingAttendance::setDate);

            attendanceRepo.save(existingAttendance);
            return ResponseEntity.ok("Attendance record updated successfully");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input while updating Attendance with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (OptimisticLockingFailureException e) {
            logger.error("Optimistic locking conflict while updating Attendance with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Entity version mismatch");
        } catch (Exception e) {
            logger.error("Error while updating Attendance with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the Attendance");
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> deleteAttendance(Long id) {
        if(id == null || id < 1L)
        {
            logger.error("Invalid ID: {}", id);
            return new ResponseEntity<>("Invalid ID provided", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!attendanceRepo.existsById(id))
            {
                return new ResponseEntity<>("Attendance record not found", HttpStatus.NOT_FOUND);
            }
            attendanceRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Attendance record deleted Successfully");
        } catch (Exception e) {
            logger.error("Error while deleting Attendance record with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Error while deleting Attendance record", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
