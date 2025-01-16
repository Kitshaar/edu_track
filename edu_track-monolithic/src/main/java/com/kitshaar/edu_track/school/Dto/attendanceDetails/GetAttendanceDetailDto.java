package com.kitshaar.edu_track.school.Dto.attendanceDetails;

import com.kitshaar.edu_track.school.models.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAttendanceDetailDto {

    private Long attendanceDetailId;
    private String studentName;
    private AttendanceStatus status;
}
