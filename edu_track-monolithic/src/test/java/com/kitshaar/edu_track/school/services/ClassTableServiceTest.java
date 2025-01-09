package com.kitshaar.edu_track.school.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClassTableServiceTest {

    @Mock
    ClassTableRepo classTableRepo;

    @InjectMocks
    ClassTableService service;

    private AutoCloseable mocks;

    //To write object as string.
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();


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
    void getAllClasses_success() throws Exception
    {
        List<ClassTable> classes = List.of(
                ClassTable.builder()
                        .className("7 Standard")
                        .build(),
                ClassTable.builder()
                        .className("10th Standard")
                        .build()
        );

        // Mock the repository method to return the above list
        Mockito.when(classTableRepo.findAll()).thenReturn(classes);

        // Act: Call the service method
        ResponseEntity<List<ClassTable>> result = service.getAllClasses();

        // Assert: Verify the result and that the repository method was called
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());  // Check if status is OK
        assertEquals(2, result.getBody().size());              // Check the size of the response body
        assertEquals("7 Standard", result.getBody().get(0).getClassName());  // Check the first class name
        assertEquals("10th Standard", result.getBody().get(1).getClassName()); // Check the second class name

        Mockito.verify(classTableRepo).findAll(); // Ensure the repository method was called

    }

    @Test
    void getAllClasses_failure() {
        // Arrange: Mock the repository to throw an exception when findAll() is called
        Mockito.when(classTableRepo.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act: Call the service method
        ResponseEntity<List<ClassTable>> result = service.getAllClasses();

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
        ResponseEntity<ClassTable> result = service.getClassById(1L);

        // Assert: Verify the result and that the repository method was called
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());  // Check if status is OK
        assertNotNull(result.getBody()); //Check if body is not null
        assertEquals("8th Standard", result.getBody().getClassName());
        assertEquals(1L, result.getBody().getClassId());


        Mockito.verify(classTableRepo).findById(1L); // Ensure the repository method was called
    }

    @Test
    void getClassById_notFound() {
        // Arrange: Mock the repository method to return Optional.empty()
        Mockito.when(classTableRepo.findById(1L)).thenReturn(Optional.empty());

        // Act: Call the service method
        ResponseEntity<ClassTable> result = service.getClassById(1L);

        // Assert: Verify the response and method call
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());  // Verify status is NOT_FOUND
        assertNull(result.getBody());  // Verify response body is null

        // Verify the repository was called
        Mockito.verify(classTableRepo).findById(1L);
    }


}

