package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.Dto.ParentTableDto;
import com.kitshaar.edu_track.school.mappers.GetMapping;
import com.kitshaar.edu_track.school.mappers.InsertMapping;
import com.kitshaar.edu_track.school.models.ParentTable;
import com.kitshaar.edu_track.school.repositories.ParentTableRepo;
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
import java.util.stream.Collectors;

@Service
public class ParentTableService {

    @Autowired
    private ParentTableRepo parentTableRepo;
    private static final Logger logger = LoggerFactory.getLogger(ParentTableService.class);

    @Transactional(transactionManager = "schoolTransactionManager", readOnly = true)
    public ResponseEntity<List<ParentTableDto>> getAllParents() {
        try {

            List<ParentTable> parents = parentTableRepo.findAll();
            List<ParentTableDto> parentTableDtos = parents.stream()
                    .map(GetMapping::mapToParentTableDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>( parentTableDtos, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error with proper details
            logger.error("Error occurred while fetching parents: {}", e.getMessage(), e);
            // Return a bad request response in case of error
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager", readOnly = true)
    public ResponseEntity<ParentTableDto> getParent(Long id) {
        try {
            return parentTableRepo.findById(id)
                    .map(parent -> {
                        //convert parent to parentDto
                        ParentTableDto parentDto = GetMapping.mapToParentTableDto(parent);
                        return ResponseEntity.ok(parentDto);
                    })
                    .orElseGet(() -> {
                        // Log the error when the parent is not found
                        logger.warn("Parent with ID {} not found.", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    });

        } catch (Exception e) {
            // Log unexpected errors with proper context
            logger.error("Unexpected error while fetching parent with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> addParent(ParentTableDto parentTableDto) {
        try {
            ParentTable parent = InsertMapping.mapToParent(parentTableDto);

            parentTableRepo.save(parent);
            return new ResponseEntity<>("Parent added successfully.", HttpStatus.OK);

        } catch (OptimisticLockingFailureException e) {
            // Handle OptimisticLockingFailureException
            logger.error("Optimistic locking failure while adding parent: {}", e.getMessage(), e);
            return new ResponseEntity<>("Optimistic Locking Failure: Entity version mismatch or entity not found.",
                    HttpStatus.CONFLICT);

        } catch (IllegalArgumentException e) {
            // Handle invalid parent ID
            logger.error("Invalid parent ID provided: {}", e.getMessage(), e);
            return new ResponseEntity<>("Invalid parent ID: " + e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            // Handle unexpected exceptions
            logger.error("Unexpected error while adding parent: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error while adding parent", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> updateParent(ParentTableDto parentTableDto, Long id) {
        if (id == null || id < 1L) {
            logger.error("Invalid ID: {}", id);
            return new ResponseEntity<>("Invalid ID provided", HttpStatus.BAD_REQUEST);
        }
        if (parentTableDto == null)
        {
            logger.error("Invalid parent details provided for ID: {}", id);
            return ResponseEntity.badRequest().body("Invalid parent details provided");
        }
        try {
                ParentTable existingParent = parentTableRepo.findById(id).orElse(null);
                if (existingParent == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Parent record with ID " + id + " not found");
                }

                //updating fields in parent record
            Optional.ofNullable(parentTableDto.getName()).ifPresent(existingParent::setName);
            Optional.ofNullable(parentTableDto.getAddress()).ifPresent(existingParent::setAddress);
            Optional.ofNullable(parentTableDto.getPhone()).ifPresent(existingParent::setPhone);
            Optional.ofNullable(parentTableDto.getAltPhone()).ifPresent(existingParent::setAltPhone);
            Optional.ofNullable(parentTableDto.getEmail()).ifPresent(existingParent::setEmail);

            //set the updated parent record
            parentTableRepo.save(existingParent);
            return ResponseEntity.ok("Parent record updated successfully");


        } catch (IllegalArgumentException e) {
            logger.error("Invalid input while updating parent with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (OptimisticLockingFailureException e) {
            logger.error("Optimistic locking conflict while updating parent with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Entity version mismatch");
        } catch (Exception e) {
            logger.error("Error while updating parent with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the parent");
        }
    }

    @Transactional(transactionManager = "schoolTransactionManager")
    public ResponseEntity<String> deleteParent(Long id) {
        if(id == null || id < 1L)
        {
            logger.error("Invalid ID: {}", id);
            return new ResponseEntity<>("Invalid ID provided", HttpStatus.BAD_REQUEST);
        }
        try {
            if (!parentTableRepo.existsById(id))
            {
                return new ResponseEntity<>("Parent record not found", HttpStatus.NOT_FOUND);
            }
            parentTableRepo.deleteById(id);
            return new ResponseEntity<>("Parent record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while deleting parent record with id {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("Error while deleting parent record", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
