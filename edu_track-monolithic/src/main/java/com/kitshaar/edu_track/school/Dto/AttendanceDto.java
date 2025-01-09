package com.kitshaar.edu_track.school.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceDto {

    private Long attendanceId;  // Include the ID in the DTO.

    @NotNull(message = "Class ID is required")
    private Long classId;  // Reference to the ClassTable using classId.

    @NotNull(message = "Date is required")
    private LocalDate date;

}
