package com.kitshaar.edu_track.school.Dto.attendances;

import com.kitshaar.edu_track.school.Dto.attendanceDetails.GetAttendanceDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAttendanceDto {

    private Long attendanceId;

    private String className;

    private LocalDate date;

    private List<GetAttendanceDetailDto> attendanceList;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
