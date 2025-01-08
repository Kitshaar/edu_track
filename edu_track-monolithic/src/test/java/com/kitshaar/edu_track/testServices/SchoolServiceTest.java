package com.kitshaar.edu_track.testServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.kitshaar.edu_track.admin.models.School;
import com.kitshaar.edu_track.admin.repositories.SchoolRepo;
import com.kitshaar.edu_track.admin.service.SchoolService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceTest {

    @Mock
    SchoolRepo schoolRepo;

    private AutoCloseable mocks;

    @InjectMocks
    SchoolService schoolService;

    //To write object as string.
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();


    School school1 = School.builder()
            .schoolId(101)
            .schoolName("Greenwood High School")
            .address("123 Elm Street, Springfield")
            .phoneNo("123-456-7890")
            .build();

    School school2 = School.builder()
            .schoolId(102)
            .schoolName("Sunrise Academy")
            .address("456 Maple Road, Rivertown")
            .phoneNo("234-567-8901")
            .build();

    School school3 = School.builder()
            .schoolId(103)
            .schoolName("Riverdale International")
            .address("789 Pine Avenue, Hilltop")
            .phoneNo("345-678-9012")
            .build();

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
    public void getAllSchools_success() throws Exception
    {


    }
}
