package com.kitshaar.edu_track.school.Dto.attendanceDetails;

import com.kitshaar.edu_track.school.models.AttendanceStatus;
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
public class AttendanceDetailDto {

    private Long attendanceDetailId;  // Include the ID in the DTO.

    @NotNull(message = "Attendance ID is required")
    private Long attendanceId;  // Reference to the Attendance entity using attendanceId.

    @NotNull(message = "Student ID is required")
    private Long studentId;  // Reference to the StudentTable using studentId.

    @NotNull(message = "Attendance status is required. Choose between PRESENT, ABSENT, LATE, EXCUSED, LEAVE")
    @Size(min = 1, message = "Attendance status cannot be empty")
    private AttendanceStatus status;  // Attendance status (e.g., present, absent).
}
