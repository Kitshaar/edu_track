package com.kitshaar.edu_track.school.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassTableDto {

    private long classID;

    @NotNull(message = "Class name is required")
    @Size(min = 1, message = "Class name cannot be empty")
    private String className;

    // Don't need references since those will be linked in other classes


}
