package com.project01.reactspring.enums;

import com.project01.reactspring.dto.response.DistrictOrStaffDTO;

import java.util.ArrayList;
import java.util.List;

public enum TypeCode {
    NOI_THAT("Nội thất"),
    NGUYEN_CAN("Nguyên căn"),
    TANG_TRET("Tầng trệt");

    private String value;

    TypeCode(String value) {
        this.value = value;
    }

    public static List<DistrictOrStaffDTO> getTypeCodes() {
        List<DistrictOrStaffDTO> typeCodes = new ArrayList<>();
        for (TypeCode typecode : TypeCode.values()) {
            typeCodes.add(DistrictOrStaffDTO.builder().label(typecode.value).value(typecode.toString()).build());
        }
        return typeCodes;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
