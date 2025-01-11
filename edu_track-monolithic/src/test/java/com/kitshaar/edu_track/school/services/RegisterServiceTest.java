package com.kitshaar.edu_track.school.services;

import com.kitshaar.edu_track.school.Dto.GetRegisterDto;
import com.kitshaar.edu_track.school.Dto.RegisterDto;
import com.kitshaar.edu_track.school.mappers.GetMapping;
import com.kitshaar.edu_track.school.mappers.InsertMapping;
import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.models.Register;
import com.kitshaar.edu_track.school.repositories.ClassTableRepo;
import com.kitshaar.edu_track.school.repositories.RegisterRepo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    RegisterRepo registerRepo;

    @Mock
    ClassTableRepo classTableRepo;

    @Mock
    GetMapping getMapping;

    @Mock
    InsertMapping insertMapping;

    @InjectMocks
    RegisterService service;

    private AutoCloseable mocks;


    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    void getAllRegisters_success() throws Exception {
        // mock list of registers
        List<Register> registers = List.of(
                Register.builder()
                        .id(1L)
                        .name("kartik")
                        .fName("Sharma ji ")
                        .mName("Mrs. Sharma ji")
                        .phone("1234567890")
                        .altPhone("0987654321")
                        .email("john.doe@example.com")
                        .address("123 Main St")
                        .classTable(ClassTable.builder().className("Final Year").build())
                        .build()
                , Register.builder()
                        .id(2L)
                        .name("Kunal")
                        .fName("Sharma ji ")
                        .mName("Mrs. Sharma ji")
                        .phone("1234557890")
                        .altPhone("0987754321")
                        .email("john.do67@example.com")
                        .address("123 Main St")
                        .classTable(ClassTable.builder().className("Final Year Bca").build())
                        .build()
        );
        // Mock the repository call
        when(registerRepo.findAllWithClassTable()).thenReturn(registers);

        // Call the method to test
        ResponseEntity<List<GetRegisterDto>> response = service.getAllRegisters();

        // Assert the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert the response body contains the mapped DTO
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Check first item in the list
        assertEquals("kartik", response.getBody().get(0).getName());
        assertEquals("Final Year", response.getBody().get(0).getClassName());

        // Check second item in the list
        assertEquals("Kunal", response.getBody().get(1).getName());
        assertEquals("Final Year Bca", response.getBody().get(1).getClassName());

        // verify all
        Mockito.verify(registerRepo).findAllWithClassTable();
    }

    @Test
    void getAllRegisters_emptyList() throws Exception {
        // Mock empty list of registers
        List<Register> registers = new ArrayList<>();

        // Mock the repository call
        when(registerRepo.findAllWithClassTable()).thenReturn(registers);

        // Call the method to test
        ResponseEntity<List<GetRegisterDto>> response = service.getAllRegisters();

        // Assert the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert the response body is empty
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Verify that the repository method was called
        Mockito.verify(registerRepo).findAllWithClassTable();
    }

    @Test
    void getAllRegisters_exception() throws Exception {
        // Mock the repository to throw an exception
        when(registerRepo.findAllWithClassTable()).thenThrow(new RuntimeException("Database error"));

        // Call the method to test
        ResponseEntity<List<GetRegisterDto>> response = service.getAllRegisters();

        // Assert the response status is BAD_REQUEST due to the exception
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Assert the response body is empty
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Verify that the repository method was called
        Mockito.verify(registerRepo).findAllWithClassTable();
    }


    @Test
    void getRegister_success() throws Exception {
        // Mock register data
        Register register = Register.builder()
                .id(1L)
                .name("kartik")
                .fName("Sharma ji")
                .mName("Mrs. Sharma ji")
                .phone("1234567890")
                .altPhone("0987654321")
                .email("john.doe@example.com")
                .address("123 Main St")
                .classTable(ClassTable.builder().className("Final Year").build())
                .build();

        // Mock the repository call to return the register
        when(registerRepo.findById(1L)).thenReturn(Optional.of(register));

        // Call the method to test
        ResponseEntity<GetRegisterDto> response = service.getRegister(1L);

        // Assert the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert the response body contains the mapped DTO
        assertNotNull(response.getBody());
        assertEquals("kartik", response.getBody().getName());
        assertEquals("Final Year", response.getBody().getClassName());

        // Verify that the repository method was called
        Mockito.verify(registerRepo).findById(1L);
    }

    @Test
    void getRegister_notFound() throws Exception {
        // Mock the repository call to return an empty Optional (register not found)
        when(registerRepo.findById(1L)).thenReturn(Optional.empty());

        // Call the method to test
        ResponseEntity<GetRegisterDto> response = service.getRegister(1L);

        // Assert the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify that the repository method was called
        Mockito.verify(registerRepo).findById(1L);
    }

    @Test
    void getRegister_exception() throws Exception {
        // Mock the repository to throw an exception when finding by ID
        when(registerRepo.findById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        // Call the method to test
        ResponseEntity<GetRegisterDto> response = service.getRegister(1L);

        // Assert the response status is INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Verify that the repository method was called
        Mockito.verify(registerRepo).findById(1L);
    }

    @Test
    void addRegister_success() throws Exception {
        // Mock the RegisterDto and ClassTable entity
        RegisterDto registerDto = RegisterDto.builder()
                .classId(1L)
                .name("kartik")
                .fatherName("Sharma ji")
                .motherName("Mrs. Sharma ji")
                .phone("1234567890")
                .altPhone("0987654321")
                .email("john.doe@example.com")
                .address("123 Main St")
                .build();

        ClassTable classTable = ClassTable.builder()
                .classId(1L)
                .className("Final Year")
                .build();

        // Mock the repository calls
        when(classTableRepo.findById(1L)).thenReturn(Optional.of(classTable));

        // Call the method to test
        ResponseEntity<String> response = service.addRegister(registerDto);

        // Assert the response status is CREATED
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Assert the response body contains the success message
        assertEquals("Register added successfully", response.getBody());

        // Verify the repository methods were called
        Mockito.verify(classTableRepo).findById(1L);
        Mockito.verify(registerRepo).save(Mockito.any(Register.class));
    }

    @Test
    void addRegister_invalidClassId() throws Exception {
        // Mock the RegisterDto
        RegisterDto registerDto = RegisterDto.builder()
                .classId(99L) // Invalid class ID
                .name("kartik")
                .fatherName("Sharma ji")
                .motherName("Mrs. Sharma ji")
                .phone("1234567890")
                .altPhone("0987654321")
                .email("john.doe@example.com")
                .address("123 Main St")
                .build();

        // Mock the repository call to return empty for the invalid class ID
        when(classTableRepo.findById(99L)).thenReturn(Optional.empty());

        // Call the method to test
        ResponseEntity<String> response = service.addRegister(registerDto);

        // Assert the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Assert the response body contains the error message
        assertTrue(response.getBody().contains("Invalid class ID"));

        // Verify the repository method was called
        Mockito.verify(classTableRepo).findById(99L);
    }
    @Test
    void addRegister_optimisticLockingFailure() throws Exception {
        // Mock the RegisterDto and ClassTable entity
        RegisterDto registerDto = RegisterDto.builder()
                .classId(1L)
                .name("kartik")
                .fatherName("Sharma ji")
                .motherName("Mrs. Sharma ji")
                .phone("1234567890")
                .altPhone("0987654321")
                .email("john.doe@example.com")
                .address("123 Main St")
                .build();

        ClassTable classTable = ClassTable.builder()
                .classId(1L)
                .className("Final Year")
                .build();

        // Mock the repository call to return the classTable
        when(classTableRepo.findById(1L)).thenReturn(Optional.of(classTable));

        // Mock optimistic locking failure (simulate the exception)
        doThrow(OptimisticLockingFailureException.class).when(registerRepo).save(Mockito.any(Register.class));

        // Call the method to test
        ResponseEntity<String> response = service.addRegister(registerDto);

        // Assert the response status is CONFLICT
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        // Assert the response body contains the optimistic locking failure message
        assertTrue(response.getBody().contains("Optimistic Locking Failure"));

        // Verify the repository method was called
        Mockito.verify(classTableRepo).findById(1L);
        Mockito.verify(registerRepo).save(Mockito.any(Register.class));
    }

    @Test
    void addRegister_exception() throws Exception {
        // Mock the RegisterDto and ClassTable entity
        RegisterDto registerDto = RegisterDto.builder()
                .classId(1L)
                .name("kartik")
                .fatherName("Sharma ji")
                .motherName("Mrs. Sharma ji")
                .phone("1234567890")
                .altPhone("0987654321")
                .email("john.doe@example.com")
                .address("123 Main St")
                .build();

        ClassTable classTable = ClassTable.builder()
                .classId(1L)
                .className("Final Year")
                .build();

        // Mock the repository call to return the classTable
        when(classTableRepo.findById(1L)).thenReturn(Optional.of(classTable));

        // Mock an unexpected exception during the save
        doThrow(new RuntimeException("Unexpected error")).when(registerRepo).save(Mockito.any(Register.class));

        // Call the method to test
        ResponseEntity<String> response = service.addRegister(registerDto);

        // Assert the response status is INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Assert the response body contains the error message
        assertTrue(response.getBody().contains("Error while adding register"));

        // Verify the repository method was called
        Mockito.verify(classTableRepo).findById(1L);
        Mockito.verify(registerRepo).save(Mockito.any(Register.class));
    }


}