package com.kitshaar.edu_track.school.Dto.students;

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
public class StudentTableDto {


    private Long studentId;  // Include the ID in the DTO.

    @NotNull(message = "Student name is required")
    @Size(min = 1, message = "Student name cannot be empty")
    private String name;

    @NotNull(message = "Parent ID is required")
    private Long parentId;  // Reference to the ParentTable using parentId.

    @NotNull(message = "Class ID is required")
    private Long classId;  // Reference to the ClassTable using classId.
}
