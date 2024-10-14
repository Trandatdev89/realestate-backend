package com.project01.reactspring.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistrictOrStaffDTO {
    private String label;
    private Object value;
}
