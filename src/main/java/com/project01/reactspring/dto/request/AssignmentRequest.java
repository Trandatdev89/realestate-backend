package com.project01.reactspring.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AssignmentRequest {
    private Long buildingId;
    private List<Long> staffId = new ArrayList<>();
}
