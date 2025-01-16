package com.kitshaar.edu_track.school.Dto;
import jakarta.validation.constraints.Email;
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
public class ParentTableDto {

    private Long parentId;  // Include the ID in the DTO.

    @NotNull(message = "Parent name is required")
    @Size(min = 1, message = "Parent name cannot be empty")
    private String name;

    @NotNull(message = "Address is required")
    @Size(min = 1, message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phone;

    @Size(min = 10, max = 10, message = "Alternate phone number must be 10 digits")
    private String altPhone;

    @Email
    private String email;

}
