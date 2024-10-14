package com.project01.reactspring.enums;

import com.project01.reactspring.dto.response.DistrictOrStaffDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum GetDistrict {
    CAU_GIAY("Quận cầu giấy"),
    THANH_XUAN("Quận thanh xuân"),
    HAI_BA_TRUNG("Quận Hai Bà Trưng"),
    BA_DINH("Quận Ba Đình"),
    LONG_BIEN("Quận Long Biên"),
    HOANG_MAI("Quận Hoàng Mai"),
    HOAN_KIEM("Quận Hoàn Kiếm"),
    HA_DONG("Quận Hà Đông");


    private String value;

    GetDistrict(String value) {
        this.value = value;
    }

    public static List<DistrictOrStaffDTO> getDistricts() {
        List<DistrictOrStaffDTO> districts = new ArrayList<>();
        for (GetDistrict district : GetDistrict.values()) {
            districts.add(DistrictOrStaffDTO.builder().label(district.value).value(district.toString()).build());
        }
        return districts;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
