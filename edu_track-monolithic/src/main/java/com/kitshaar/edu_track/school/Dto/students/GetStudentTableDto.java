package com.kitshaar.edu_track.school.Dto.students;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStudentTableDto {

    private Long studentId;

    private String name;

    private String parentName;

    private String address;

    private String className;

}

