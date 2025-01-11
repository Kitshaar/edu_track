package com.kitshaar.edu_track.school.Dto.registers;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {

    private Long id;
    @NotNull(message = "Name is required")
    @Size(min = 1, message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Father's name is required")
    @Size(min = 1, message = "Father's name cannot be empty")
    private String fatherName;

    @NotNull(message = "Mother's name is required")
    @Size(min = 1, message = "Mother's name cannot be empty")
    private String motherName;

    @NotNull(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phone;

    @Size(min = 10, max = 10, message = "Alternate phone number must be 10 digits")
    private String altPhone;

    private String email;

    @NotNull(message = "Address is required")
    @Size(min = 1, message = "Address cannot be empty")
    private String address;

    private Long classId;

    // no created and updated at because I will implement updated at in service method
}
