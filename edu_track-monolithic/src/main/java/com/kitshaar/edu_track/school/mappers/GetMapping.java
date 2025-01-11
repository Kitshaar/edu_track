package com.kitshaar.edu_track.school.mappers;

import com.kitshaar.edu_track.school.Dto.registers.GetRegisterDto;
import com.kitshaar.edu_track.school.models.Register;


public class GetMapping {

    public static GetRegisterDto mapToGetRegisterDto(Register register) {
        return GetRegisterDto.builder()
                .id(register.getId())
                .name(register.getName())
                .fatherName(register.getFName())
                .motherName(register.getMName())
                .phone(register.getPhone())
                .altPhone(register.getAltPhone())
                .email(register.getEmail())
                .address(register.getAddress())
                .className(register.getClassTable() != null ? register.getClassTable().getClassName() : null)
                .createdAt(register.getCreatedAt())
                .updatedAt(register.getUpdatedAt())
                .build();
    }
}
