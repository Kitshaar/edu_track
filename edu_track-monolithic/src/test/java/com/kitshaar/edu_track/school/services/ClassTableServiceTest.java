package com.kitshaar.edu_track.school.services;


import com.kitshaar.edu_track.school.Dto.ClassTableDto;
import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.repositories.ClassTableRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
class ClassTableServiceTest {

    @Mock
    ClassTableRepo classTableRepo;

    @InjectMocks
    ClassTableService service;

    private AutoCloseable mocks;



    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    void getAllClasses_success() throws Exception {
        // Mock ClassTable entities
        List<ClassTable> classes = List.of(
                ClassTable.builder()
                        .classId(1L)
                        .className("7 Standard")
                        .build(),
                ClassTable.builder()
                        .classId(1L)
                        .className("10th Standard")
                        .build()
        );

        // Mock the repository method to return the above list
        Mockito.when(classTableRepo.findAll()).thenReturn(classes);

        // Act: Call the service method
        ResponseEntity<List<ClassTableDto>> result = service.getAllClasses();

        // Assert: Verify the result and that the repository method was called
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());  // Check if status is OK
        assertEquals(2, result.getBody().size());              // Check the size of the response body

        // Check the class names in the response
        assertEquals("7 Standard", result.getBody().get(0).getClassName());  // Check the first class name
        assertEquals("10th Standard", result.getBody().get(1).getClassName()); // Check the second class name

        // Verify repository method was called
        Mockito.verify(classTableRepo).findAll();
    }


    @Test
    void getAllClasses_failure() {
        // Arrange: Mock the repository to throw an exception when findAll() is called
        Mockito.when(classTableRepo.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act: Call the service method
        ResponseEntity<List<ClassTableDto>> result = service.getAllClasses();

        // Assert: Verify that the result is not null and has a BAD_REQUEST status
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());  // Check if status is BAD_REQUEST
        assertTrue(result.getBody().isEmpty());                          // Check if the response body is empty

        // Verify that the repository method was called
        Mockito.verify(classTableRepo).findAll();
    }

    @Test
    void getClassById_success() throws Exception
    {
        ClassTable class1 = ClassTable.builder()
            .classId(1L)
            .className("8th Standard")
            .build();

        // Mock the repository method to return the above object
        Mockito.when(classTableRepo.findById(1L)).thenReturn(Optional.of(class1));

        // Act: Call the service method
        ResponseEntity<ClassTableDto> result = service.getClassById(1L);

        // Assert: Verify the result and that the repository method was called
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());  // Check if status is OK
        assertNotNull(result.getBody()); //Check if body is not null
        assertEquals("8th Standard", result.getBody().getClassName());
        assertEquals(1L, result.getBody().getClassID());


        Mockito.verify(classTableRepo).findById(1L); // Ensure the repository method was called
    }

    @Test
    void getClassById_notFound() {
        // Arrange: Mock the repository method to return Optional.empty()
        Mockito.when(classTableRepo.findById(1L)).thenReturn(Optional.empty());

        // Act: Call the service method
        ResponseEntity<ClassTableDto> result = service.getClassById(1L);

        // Assert: Verify the response and method call
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());  // Verify status is NOT_FOUND
        assertNull(result.getBody());  // Verify response body is null

        // Verify the repository was called
        Mockito.verify(classTableRepo).findById(1L);
    }


    @Test
    void addClass_success() throws Exception {
        // Arrange: Create a mock ClassTableDto and the expected ClassTable
        ClassTableDto newClassDto = ClassTableDto.builder()
                .className("9th Standard")
                .build();

        ClassTable newClassEntity = ClassTable.builder()
                .className("9th Standard")
                .build();

        // Mock the repository save method to return the same entity
        Mockito.when(classTableRepo.save(Mockito.any(ClassTable.class))).thenReturn(newClassEntity);

        // Act: Call the service method
        ResponseEntity<String> result = service.addClass(newClassDto);

        // Assert: Verify the response
        assertNotNull(result);                                      // Ensure response is not null
        assertEquals(HttpStatus.CREATED, result.getStatusCode());   // Verify status is CREATED
        assertEquals("Class added successfully", result.getBody()); // Verify success message

        // Verify the repository save method was called
        Mockito.verify(classTableRepo).save(Mockito.any(ClassTable.class));
    }


    @Test
    void addClass_nullInput() {
        // Act: Call the service method with null input
        ResponseEntity<String> result = service.addClass(null);

        // Assert: Verify the response
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()); // Verify status is BAD_REQUEST
        assertEquals("Invalid class details: class value or name is missing", result.getBody());      // Verify error message

        // Verify the repository save method was NOT called
        Mockito.verify(classTableRepo, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addClass_optimisticLockingFailure() {
        // Arrange: Mock the repository to throw an OptimisticLockingFailureException

        ClassTableDto newClassDto = ClassTableDto.builder()
                .className("9th Standard")
                .build();

        ClassTable newClassEntity = ClassTable.builder()
                .className("9th Standard")
                .build();
        Mockito.when(classTableRepo.save(newClassEntity))
                .thenThrow(new OptimisticLockingFailureException("Entity version mismatch"));

        // Act: Call the service method
        ResponseEntity<String> result = service.addClass(newClassDto);

        // Assert: Verify the response
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());      // Verify status is CONFLICT
        assertEquals("Optimistic Locking Failure: Entity version mismatch or entity not found.", result.getBody());

        // Verify the repository save method was called
        Mockito.verify(classTableRepo).save(newClassEntity);
    }


    @Test
    void addClass_failure() {
        // Arrange: Create a mock ClassTableDto
        ClassTableDto newClassDto = ClassTableDto.builder()
                .className("9th Standard")
                .build();

        // Mock the repository save method to throw a RuntimeException
        Mockito.when(classTableRepo.save(Mockito.any(ClassTable.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act: Call the service method
        ResponseEntity<String> result = service.addClass(newClassDto);

        // Assert: Verify the response
        assertNotNull(result);                                            // Ensure response is not null
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode()); // Verify status is INTERNAL_SERVER_ERROR
        assertEquals("Error while adding class", result.getBody());             // Verify error message

        // Verify the repository save method was called
        Mockito.verify(classTableRepo).save(Mockito.any(ClassTable.class));
    }


    @Test
    void deleteClass_success() {
        // Arrange: Mock the repository behavior
        Long classId = 1L;
        Mockito.when(classTableRepo.existsById(classId)).thenReturn(true);

        // Act: Call the service method
        ResponseEntity<String> result = service.deleteClass(classId);

        // Assert: Verify the response
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());              // Verify status is OK
        assertEquals("Class deleted successfully", result.getBody());    // Verify success message

        // Verify that the repository deleteById method was called
        Mockito.verify(classTableRepo).deleteById(classId);
    }

    @Test
    void deleteClass_notFound() {
        // Arrange: Mock the repository behavior for a non-existing class
        Long classId = 2L;
        Mockito.when(classTableRepo.existsById(classId)).thenReturn(false);

        // Act: Call the service method
        ResponseEntity<String> result = service.deleteClass(classId);

        // Assert: Verify the response
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());      // Verify status is NOT_FOUND
        assertEquals("Class not found", result.getBody());               // Verify error message

        // Verify that the repository deleteById method was NOT called
        Mockito.verify(classTableRepo, Mockito.never()).deleteById(classId);
    }

    @Test
    void deleteClass_exception() {
        // Arrange: Mock the repository behavior to throw an exception
        Long classId = 3L;
        Mockito.when(classTableRepo.existsById(classId)).thenReturn(true);
        Mockito.doThrow(new RuntimeException("Database error")).when(classTableRepo).deleteById(classId);

        // Act: Call the service method
        ResponseEntity<String> result = service.deleteClass(classId);

        // Assert: Verify the response
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());  // Verify status is INTERNAL_SERVER_ERROR
        assertEquals("Error while deleting class", result.getBody());            // Verify error message

        // Verify that the repository deleteById method was called
        Mockito.verify(classTableRepo).deleteById(classId);
    }

    @Test
    void deleteClass_nullId() {
        // Act: Call the service method with a null ID
        ResponseEntity<String> result = service.deleteClass(null);

        // Assert: Verify the response
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());    // Verify status is BAD_REQUEST
        assertEquals("Invalid ID provided", result.getBody());          // Verify error message

        // Verify the repository deleteById method was NOT called
        Mockito.verify(classTableRepo, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    void deleteClass_invalidId() {
        // Act: Call the service method with an invalid ID
        ResponseEntity<String> result = service.deleteClass(0L);

        // Assert: Verify the response
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());    // Verify status is BAD_REQUEST
        assertEquals("Invalid ID provided", result.getBody());          // Verify error message

        // Verify the repository deleteById method was NOT called
        Mockito.verify(classTableRepo, Mockito.never()).deleteById(Mockito.anyLong());
    }


    @Test
    void updateUser_nullId() {
        // Arrange: Create a valid ClassTableDto object
        ClassTableDto classTableDto = ClassTableDto.builder()
                .className("Some Name")
                .build();

        // Act: Call the service method with a null ID
        ResponseEntity<String> result = service.updateUser(null, classTableDto);

        // Assert: Verify the response
        assertNotNull(result);                                // Ensure response is not null
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());  // Verify status is BAD_REQUEST
        assertEquals("Invalid ID provided", result.getBody()); // Verify the error message
    }


    @Test
    void updateUser_nullClassTable() {
        ResponseEntity<String> result = service.updateUser(1L, null);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid class details provided", result.getBody());
    }

    @Test
    void updateUser_success() {
        // Arrange: Create the DTO for the update
        ClassTableDto updatedClassDto = ClassTableDto.builder()
                .classID(1L)
                .className("New Name")
                .build();

        // Create the existing class entity in the database (this is the one we simulate as existing)
        ClassTable existingClass = ClassTable.builder()
                .classId(1L)
                .className("Old Name")
                .build();

        // Create the updated class entity after the update
        ClassTable updatedClass = ClassTable.builder()
                .classId(1L)
                .className("New Name")
                .build();

        // Mock the repository methods
        Mockito.when(classTableRepo.findById(1L)).thenReturn(Optional.of(existingClass));  // Simulate the class is found
        Mockito.when(classTableRepo.save(Mockito.any(ClassTable.class))).thenReturn(updatedClass);  // Simulate saving the updated class

        // Act: Call the service method
        ResponseEntity<String> result = service.updateUser(1L, updatedClassDto);

        // Assert: Verify the response
        assertNotNull(result);                                         // Ensure response is not null
        assertEquals(HttpStatus.OK, result.getStatusCode());           // Verify status is OK
        assertEquals("Class updated successfully", result.getBody());  // Verify the response message

        // Verify the repository methods were called
        Mockito.verify(classTableRepo).findById(1L);      // Ensure findById was called
        Mockito.verify(classTableRepo).save(Mockito.any(ClassTable.class));  // Ensure save was called

        // Further verification (optional): Ensure the save method was called with the correct entity
        Mockito.verify(classTableRepo).save(argThat(classTable ->
                classTable.getClassId().equals(1L) && classTable.getClassName().equals("New Name")
        ));
    }







}

