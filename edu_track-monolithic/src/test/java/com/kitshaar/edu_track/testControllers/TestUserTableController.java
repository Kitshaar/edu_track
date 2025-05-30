package com.kitshaar.edu_track.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.kitshaar.edu_track.admin.controllers.UserTableController;
import com.kitshaar.edu_track.admin.models.UserTable;
import com.kitshaar.edu_track.admin.service.UserTableService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TestUserTableController {

    private MockMvc mockMvc;
    private AutoCloseable mocks;

    //To write object as string.
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private UserTableService userTableService;

    @InjectMocks
    private UserTableController userTableController;

    //Creating some demo UserTable objects for tests

    UserTable user1 = UserTable.builder()
            .id(1)
            .name("Alice Smith")
            .email("alice.smith@example.com")
            .password("securepassword123")
            .hashPassword("hashedpassword123")
            .createdAt(OffsetDateTime.now())
            .updatedAt(LocalDate.of(2025, 1, 6))
            .role("ADMIN")
            .build();

    UserTable user2 = UserTable.builder()
            .id(2)
            .name("Bob Johnson")
            .email("bob.johnson@example.com")
            .password("mypassword")
            .hashPassword("hashedmypassword")
            .createdAt(OffsetDateTime.now())
            .updatedAt(LocalDate.of(2025, 1, 7))
            .role("USER")
            .build();

    UserTable user3 = UserTable.builder()
            .id(3)
            .name("Charlie Brown")
            .email("charlie.brown@example.com")
            .password("passwordcharlie")
            .hashPassword("hashedpasswordcharlie")
            .createdAt(OffsetDateTime.now())
            .updatedAt(LocalDate.of(2025, 1, 5))
            .role("MODERATOR")
            .build();

    UserTable user4 = UserTable.builder()
            .id(4)
            .name("Diana Prince")
            .email("diana.prince@example.com")
            .password("wonderwoman")
            .hashPassword("hashedwonderwoman")
            .createdAt(OffsetDateTime.now())
            .updatedAt(LocalDate.of(2025, 1, 4))
            .role("USER")
            .build();

    UserTable user5 = UserTable.builder()
            .id(5)
            .name("Eve Adams")
            .email("eve.adams@example.com")
            .password("evepassword")
            .hashPassword("hashedevepassword")
            .createdAt(OffsetDateTime.now())
            .updatedAt(LocalDate.of(2025, 1, 3))
            .role("ADMIN")
            .build();

    @BeforeEach
    public void setUp() {

        mocks = MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userTableController).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }

    }

    @Test
    public void getAllUsers_success() throws Exception
    {
        List<UserTable> userList = new ArrayList<>(Arrays.asList(user1,user2,user3,user4,user5));
        ResponseEntity<List<UserTable>> users  = new ResponseEntity<>(userList, HttpStatus.OK);
        Mockito.when(userTableService.getAllUsers()).thenReturn(users);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/admin/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[3].name", is("Diana Prince")));

        Mockito.verify(userTableService, Mockito.times(1)).getAllUsers();

    }

    @Test
    public void getAllUsers_fail()
    {
        ResponseEntity<List<UserTable>> users  = new ResponseEntity<>( new ArrayList<>(), HttpStatus.BAD_REQUEST);
        Mockito.when(userTableService.getAllUsers()).thenReturn(users);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/admin/users")
                .contentType(MediaType.APPLICATION_JSON);

        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$", empty()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        Mockito.verify(userTableService, Mockito.times(1)).getAllUsers();
    }

    @Test
    public void getUser_success() throws Exception
    {
        ResponseEntity<UserTable> user = new ResponseEntity<>(user1, HttpStatus.OK);
        Mockito.when(userTableService.getUser(user1.getId())).thenReturn(user);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/admin/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(user1.getName())));

        Mockito.verify(userTableService, Mockito.times(1)).getUser(user1.getId());
    }

    @Test
    public void getUser_fail()
    {
        ResponseEntity<UserTable> user  = new ResponseEntity<>( new UserTable(), HttpStatus.BAD_REQUEST);
        Mockito.when(userTableService.getUser(8)).thenReturn(user);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/admin/users/8")
                .contentType(MediaType.APPLICATION_JSON);

        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isBadRequest());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Mockito.verify(userTableService, Mockito.times(1)).getUser(8);
    }

    @Test
    public void addUser_success() throws Exception
    {
        UserTable userTable = UserTable.builder()
                .id(6)
                .name("Kit")
                .email("Kit.adams@example.com")
                .password("kitpassword")
                .hashPassword("hashedkitpassword")
                .role("ADMIN")
                .build();

        Mockito.when(userTableService.addUser(userTable)).thenReturn(new ResponseEntity<>("Success", HttpStatus.CREATED));

        String content = objectWriter.writeValueAsString(userTable);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(content().string("Success"));

        Mockito.verify(userTableService, Mockito.times(1)).addUser(userTable);

    }

    @Test
    public void addUser_nullUser_failure() throws Exception {
        Mockito.when(userTableService.addUser(new UserTable()))
                .thenReturn(new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Entity being sent is null. It is not allowed"));
    }


    @Test
    public void updateUser_success() throws Exception {
        UserTable updatedUser = UserTable.builder()
                .id(1)
                .name("New Name")
                .email("new.email@example.com")
                .password("newpassword")
                .hashPassword("hashedNewPassword")
                .role("ADMIN")
                .build();

        Mockito.when(userTableService.updateUser(1, updatedUser))
                .thenReturn(new ResponseEntity<>("User updated successfully.", HttpStatus.OK));

        String content = objectWriter.writeValueAsString(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/admin/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully."));

        Mockito.verify(userTableService, Mockito.times(1)).updateUser(1, updatedUser);
    }

    @Test
    public void updateUser_notFullUserObjectProvided_success() throws Exception {
        UserTable updatedUser = UserTable.builder()
                .email("new.kit@example.com")
                .build();

        Mockito.when(userTableService.updateUser(1, updatedUser))
                .thenReturn(new ResponseEntity<>("User updated successfully.", HttpStatus.OK));

        String content = objectWriter.writeValueAsString(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/admin/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully."));

        Mockito.verify(userTableService, Mockito.times(1)).updateUser(1, updatedUser);
    }

    @Test
    public void updateUser_invalidId_failure() throws Exception {
        UserTable updatedUser = UserTable.builder()
                .id(1)
                .name("New Name")
                .email("new.email@example.com")
                .password("newpassword")
                .hashPassword("hashedNewPassword")
                .role("ADMIN")
                .build();

        Mockito.when(userTableService.updateUser(999, updatedUser))
                .thenReturn(new ResponseEntity<>("Id being sent is null or doesn't exist. It is not allowed", HttpStatus.BAD_REQUEST));

        String content = objectWriter.writeValueAsString(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/admin/users/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Id being sent is null or doesn't exist. It is not allowed"));

        Mockito.verify(userTableService, Mockito.times(1)).updateUser(999, updatedUser);
    }

    @Test
    public void updateUser_nullUser_failure() throws Exception {
        Mockito.when(userTableService.updateUser(1, new UserTable()))
                .thenReturn(new ResponseEntity<>("Entity being sent is null. It is not allowed", HttpStatus.BAD_REQUEST));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/admin/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}")) // Empty request body to simulate null user
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Entity being sent is null. It is not allowed"));

        Mockito.verify(userTableService, Mockito.times(1)).updateUser(1, new UserTable());
    }

    @Test
    public void deleteUser_success() throws Exception {
        Integer userId = 1;

        // Mocking the service behavior
        Mockito.when(userTableService.deleteUser(userId))
                .thenReturn(new ResponseEntity<>("Successful deletion", HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/users/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successful deletion"));

        Mockito.verify(userTableService, Mockito.times(1)).deleteUser(userId);
    }

    @Test
    public void deleteUser_userNotFound() throws Exception {
        Integer userId = 999; // ID that does not exist in the database

        // Mocking the service behavior to return "User not found"
        Mockito.when(userTableService.deleteUser(userId))
                .thenReturn(new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/users/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        Mockito.verify(userTableService, Mockito.times(1)).deleteUser(userId);
    }

}
