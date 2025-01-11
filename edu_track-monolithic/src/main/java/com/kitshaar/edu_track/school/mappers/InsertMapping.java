package com.kitshaar.edu_track.school.mappers;

import com.kitshaar.edu_track.school.Dto.registers.RegisterDto;
import com.kitshaar.edu_track.school.models.ClassTable;
import com.kitshaar.edu_track.school.models.Register;

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
}
