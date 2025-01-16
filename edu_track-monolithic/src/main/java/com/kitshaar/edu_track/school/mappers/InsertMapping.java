package com.kitshaar.edu_track.school.mappers;

import com.kitshaar.edu_track.school.Dto.ParentTableDto;
import com.kitshaar.edu_track.school.Dto.attendances.AttendanceDto;
import com.kitshaar.edu_track.school.Dto.registers.RegisterDto;
import com.kitshaar.edu_track.school.Dto.students.StudentTableDto;
import com.kitshaar.edu_track.school.models.*;

public class InsertMapping {

    public static Register mapToRegister(RegisterDto registerDto, ClassTable classTable) {
            // Ensure classTable is not null before proceeding
            if (classTable == null) {
                throw new IllegalArgumentException("ClassTable cannot be null");
            }


            return Register.builder()
                    .name(registerDto.getName())
                    .fName(registerDto.getFatherName())
                    .mName(registerDto.getMotherName())
                    .phone(registerDto.getPhone())
                    .altPhone(registerDto.getAltPhone())
                    .email(registerDto.getEmail())
                    .address(registerDto.getAddress())
                    .classTable(classTable) // Map the ClassTable entity
                    .build();
        }

    public static ParentTable mapToParent(ParentTableDto parentTableDto)
    {
        return ParentTable.builder()
                .parentId(parentTableDto.getParentId())
                .name(parentTableDto.getName())
                .address(parentTableDto.getAddress())
                .phone(parentTableDto.getPhone())
                .altPhone(parentTableDto.getAltPhone())
                .email(parentTableDto.getEmail())
                .build();
    }

    public static StudentTable maptoStudent(StudentTableDto studentTableDto, ClassTable classTable, ParentTable parent )
    {
        // Ensure classTable is not null before proceeding
        if (classTable == null) {
            throw new IllegalArgumentException("ClassTable cannot be null");
        }
        // Ensure parent is not null before proceeding
        if (parent == null) {
            throw new IllegalArgumentException("Parent cannot be null");
        }

        return StudentTable.builder()
                .studentId(studentTableDto.getStudentId())
                .name(studentTableDto.getName())
                .classTable(classTable)
                .parent(parent)
                .build();
    }

    public static Attendance mapToAttendance(AttendanceDto attendanceDto, ClassTable classTable)
    {
        // Ensure classTable is not null before proceeding
        if (classTable == null) {
            throw new IllegalArgumentException("ClassTable cannot be null");
        }

        return Attendance.builder()
                .attendanceId(attendanceDto.getAttendanceId())
                .date(attendanceDto.getDate())
                .classTable(classTable)
                .build();
    }
}
