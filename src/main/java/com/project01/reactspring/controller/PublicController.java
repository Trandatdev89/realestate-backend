package com.project01.reactspring.controller;


import com.project01.reactspring.dto.response.DistrictOrStaffDTO;
import com.project01.reactspring.enums.GetDistrict;
import com.project01.reactspring.enums.TypeCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PublicController {

    @GetMapping("/api/district")
    public List<DistrictOrStaffDTO> district() {
        return GetDistrict.getDistricts();
    }

    @GetMapping("/api/typecode")
    public List<DistrictOrStaffDTO> typeCode() {
        return TypeCode.getTypeCodes();
    }

}
