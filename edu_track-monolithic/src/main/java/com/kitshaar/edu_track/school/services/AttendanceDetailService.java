package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.Dto.attendanceDetails.AttendanceDetailDto;
import com.kitshaar.edu_track.school.Dto.attendanceDetails.GetAttendanceDetailDto;
import com.kitshaar.edu_track.school.Dto.attendances.GetAttendanceDto;
import com.kitshaar.edu_track.school.mappers.GetMapping;
import com.kitshaar.edu_track.school.mappers.InsertMapping;
import com.kitshaar.edu_track.school.models.Attendance;
import com.kitshaar.edu_track.school.models.AttendanceDetail;
import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.models.StudentTable;
import com.kitshaar.edu_track.school.repositories.AttendanceDetailRepo;
import com.kitshaar.edu_track.school.repositories.AttendanceRepo;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceDetailService {

    @Autowired
    private AttendanceDetailRepo attendanceDetailRepo;

    @Autowired
    private StudentTableRepo studentTableRepo;

    @Autowired
    private AttendanceRepo attendanceRepo;

    private static final Logger logger = LoggerFactory.getLogger(AttendanceDetailService.class);

    @Transactional(transactionManager = "schoolTransactionManager", readOnly = true)
    public ResponseEntity<List<GetAttendanceDetailDto>> getAllAttendanceDetails() {
        try {
            List<AttendanceDetail> attendanceDetails = attendanceDetailRepo.findAllWithStudentTable();
            List<GetAttendanceDetailDto> attendanceDetailDtoList = attendanceDetails.stream()
                    .map(GetMapping::mapToAttendanceDetailDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(attendanceDetailDtoList, HttpStatus.OK);
        }
        catch (Exception e) {
            // Log the error with proper details
            logger.error("Error occurred while fetching attendanceDetails: {}", e.getMessage(), e);
            // Return a bad request response in case of error
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager", readOnly = true)
    public ResponseEntity<GetAttendanceDetailDto> getAttendanceDetail(Long id) {
        try {
            return attendanceDetailRepo.findById(id)
                    .map(attendanceDetail -> {
                        GetAttendanceDetailDto attendanceDetailDto = GetMapping.mapToAttendanceDetailDto(attendanceDetail);
                        return ResponseEntity.ok(attendanceDetailDto);
                    })
                    .orElseGet(() -> {
                        // Log the error when the attendanceDetail is not found
                        logger.warn("AttendanceDetail with ID {} not found.", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    });
        } catch (Exception e) {
            // Log unexpected errors with proper context
            logger.error("Unexpected error while fetching attendanceDetail with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> addAttendanceDetail(@Valid AttendanceDetailDto attendanceDetailDto) {
        try {

            // Fetch the StudentTable entity by ID
            StudentTable student = studentTableRepo.findById(attendanceDetailDto.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: "
                            + attendanceDetailDto.getStudentId()));

            // Fetch the Attendance entity by ID
            Attendance attendance = attendanceRepo.findById(attendanceDetailDto.getAttendanceId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Attendance ID:"
                            + attendanceDetailDto.getAttendanceId()));

            AttendanceDetail attendanceDetail = InsertMapping.mapToAttendanceDetail
                    (attendanceDetailDto, student, attendance);

            attendanceDetailRepo.save(attendanceDetail);
            return ResponseEntity.status(HttpStatus.CREATED).body("AttendanceDetail added successfully.");
        } catch (OptimisticLockingFailureException e) {
            // Handle OptimisticLockingFailureException
            logger.error("Optimistic locking failure while adding attendanceDetail: {}", e.getMessage(), e);
            return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
                    HttpStatus.CONFLICT);

        } catch (IllegalArgumentException e) {
            // Handle invalid attendanceDetail ID
            logger.error("Invalid attendanceDetail ID provided: {}", e.getMessage(), e);
            return new ResponseEntity<>("Invalid attendanceDetail ID: " + e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            // Handle unexpected exceptions
            logger.error("Unexpected error while adding attendanceDetail : {}", e.getMessage(), e);
            return new ResponseEntity<>("Error while adding attendanceDetail ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> updateAttendanceDetail(Long id, AttendanceDetailDto attendanceDetailDto) {
        if (id == null || id < 1L) {
            logger.error("Invalid ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid ID provided");
        }
        if (attendanceDetailDto == null) {
            logger.error("Invalid AttendanceDetail values provided for ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid AttendanceDetail values provided");
        }
        try {
            AttendanceDetail existingAttendanceDetail = attendanceDetailRepo.findById(id).orElse(null);

            if (existingAttendanceDetail == null)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("AttendanceDetail record with ID " + id + " not found");
            }

            //Validate and set Student
            if (attendanceDetailDto.getStudentId() != null && attendanceDetailDto.getStudentId() > 0)
            {
                StudentTable student = studentTableRepo.findById(attendanceDetailDto.getStudentId())
                        .orElseThrow(() -> new
                                IllegalArgumentException
                                ("Invalid student ID: " + attendanceDetailDto.getStudentId() + " Provided.")
                        );
                existingAttendanceDetail.setStudent(student);
            }

            //Validate and set Attendance
            if (attendanceDetailDto.getAttendanceId() != null && attendanceDetailDto.getAttendanceId() > 0)
            {
                Attendance attendance = attendanceRepo.findById(attendanceDetailDto.getAttendanceId())
                        .orElseThrow(() -> new
                                IllegalArgumentException
                                ("Invalid attendance ID: " + attendanceDetailDto.getAttendanceId() + " Provided.")
                        );
                existingAttendanceDetail.setAttendance(attendance);
            }

            //Update the rest of fields in AttendanceDetail
            Optional.ofNullable(attendanceDetailDto.getStatus()).ifPresent(existingAttendanceDetail::setStatus);

            attendanceDetailRepo.save(existingAttendanceDetail);
            return ResponseEntity.ok("AttendanceDetail record updated successfully");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input while updating AttendanceDetail with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (OptimisticLockingFailureException e) {
            logger.error("Optimistic locking conflict while updating AttendanceDetail with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Entity version mismatch");
        } catch (Exception e) {
            logger.error("Error while updating AttendanceDetail with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the AttendanceDetail");
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> deleteAttendanceDetail(Long id) {
        if(id == null || id < 1L)
        {
            logger.error("Invalid ID: {}", id);
            return new ResponseEntity<>("Invalid ID provided", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!attendanceDetailRepo.existsById(id))
            {
                return new ResponseEntity<>("AttendanceDetail record not found", HttpStatus.NOT_FOUND);
            }
            attendanceDetailRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("AttendanceDetail record deleted Successfully");
        } catch (Exception e) {
            logger.error("Error while deleting AttendanceDetail record with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Error while deleting AttendanceDetail record", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
