package com.kitshaar.edu_track.school.services;


import com.kitshaar.edu_track.school.Dto.registers.GetRegisterDto;
import com.kitshaar.edu_track.school.Dto.registers.RegisterDto;
import com.kitshaar.edu_track.school.mappers.GetMapping;
import com.kitshaar.edu_track.school.mappers.InsertMapping;
import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.models.Register;
import com.kitshaar.edu_track.school.repositories.ClassTableRepo;
import com.kitshaar.edu_track.school.repositories.RegisterRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegisterService {

    @Autowired
    private RegisterRepo registerRepo;

    @Autowired
    private ClassTableRepo classTableRepo;

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<List<GetRegisterDto>> getAllRegisters() {

        try {
            List<Register> registers = registerRepo.findAllWithClassTable();
            // Map register entities to DTOs
            List<GetRegisterDto> getRegisterDtos = registers.stream()
                    .map(GetMapping::mapToGetRegisterDto)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(getRegisterDtos, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error with proper details
            logger.error("Error occurred while fetching registers: {}", e.getMessage(), e);
            // Return a bad request response in case of error
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<GetRegisterDto> getRegister(Long id) {
        try {
            // Find register by ID
            return registerRepo.findById(id)
                    .map(register -> {
                        // Convert the Register entity to GetRegisterDto
                        GetRegisterDto getRegisterDto = GetMapping.mapToGetRegisterDto(register); //  method call
                        return ResponseEntity.ok(getRegisterDto);
                    })
                    .orElseGet(() -> {
                        // Log the error when the register is not found
                        logger.warn("Register with ID {} not found.", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    });
        } catch (Exception e) {
            // Log unexpected errors with proper context
            logger.error("Unexpected error while fetching register with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> addRegister(RegisterDto registerDto) {

            try {
                // Fetch the ClassTable entity by ID
                ClassTable classTable = classTableRepo.findById(registerDto.getClassId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid class ID: " + registerDto.getClassId()));

                // Map RegisterDto to Register entity
                Register register = InsertMapping.mapToRegister(registerDto, classTable);

                // Save the Register entity
                registerRepo.save(register);

                return new ResponseEntity<>("Register added successfully", HttpStatus.CREATED);
            } catch (OptimisticLockingFailureException e) {
                // Handle OptimisticLockingFailureException
                logger.error("Optimistic locking failure while adding register: {}", e.getMessage(), e);
                return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
                        HttpStatus.CONFLICT);

            } catch (IllegalArgumentException e) {
                // Handle invalid class ID
                logger.error("Invalid class ID provided: {}", e.getMessage(), e);
                return new ResponseEntity<>("Invalid class ID: " + e.getMessage(), HttpStatus.BAD_REQUEST);

            } catch (Exception e) {
                // Handle unexpected exceptions
                logger.error("Unexpected error while adding register: {}", e.getMessage(), e);
                return new ResponseEntity<>("Error while adding register", HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }


    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> update(Long id, RegisterDto registerDto) {

        if (id == null || id < 1L) {
            logger.error("Invalid ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid ID provided");
        }
        if (registerDto == null) {
            logger.error("Invalid register details provided for ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid register details provided");
        }

        try {
            // Check if the register record exists
            Register existingRegister = registerRepo.findById(id).orElse(null);
            if (existingRegister == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Register record with ID " + id + " not found");
            }

            // Validate and set ClassTable
            if (registerDto.getClassId() != null && registerDto.getClassId() > 0) {
                ClassTable classTable = classTableRepo.findById(registerDto.getClassId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid class ID: " + registerDto.getClassId()));
                existingRegister.setClassTable(classTable);
            }

            // Update fields in Register
            existingRegister.setUpdatedAt(LocalDate.now());
            Optional.ofNullable(registerDto.getName()).ifPresent(existingRegister::setName);
            Optional.ofNullable(registerDto.getFatherName()).ifPresent(existingRegister::setFName);
            Optional.ofNullable(registerDto.getMotherName()).ifPresent(existingRegister::setMName);
            Optional.ofNullable(registerDto.getPhone()).ifPresent(existingRegister::setPhone);
            Optional.ofNullable(registerDto.getAltPhone()).ifPresent(existingRegister::setAltPhone);
            Optional.ofNullable(registerDto.getEmail()).ifPresent(existingRegister::setEmail);
            Optional.ofNullable(registerDto.getAddress()).ifPresent(existingRegister::setAddress);

            // Save the updated register
            registerRepo.save(existingRegister);
            return ResponseEntity.ok("Register record updated successfully");

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
    public ResponseEntity<String> deleteRegister(Long id) {

        if(id == null || id < 1L)
        {
            logger.error("Invalid ID: {}", id);
            return new ResponseEntity<>("Invalid ID provided", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!registerRepo.existsById(id)) {
                return new ResponseEntity<>("Register record not found", HttpStatus.NOT_FOUND);
            }
            registerRepo.deleteById(id);
            return new ResponseEntity<>("Register record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while deleting register record with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Error while deleting register record", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
