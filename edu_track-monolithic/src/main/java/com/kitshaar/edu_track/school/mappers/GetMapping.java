package com.kitshaar.edu_track.school.mappers;

import com.kitshaar.edu_track.school.Dto.ParentTableDto;
import com.kitshaar.edu_track.school.Dto.registers.GetRegisterDto;
import com.kitshaar.edu_track.school.models.ParentTable;
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
    public static ParentTableDto mapToParentTableDto(ParentTable parentTable)
    {
        return ParentTableDto.builder()
                .parentId(parentTable.getParentId())
                .name(parentTable.getName())
                .address(parentTable.getAddress())
                .phone(parentTable.getPhone())
                .altPhone(parentTable.getAltPhone())
                .email(parentTable.getEmail())
                .build();
    }
}
