package com.project01.reactspring.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildingResponseDTO {
    private Long id;
    private String createdDate;
    private String name;
    private String address;
    private Long numberOfBasement;
    private String managerName;
    private String managerPhone;
    private Long floorArea;
    private String rentAreaString;
    private String emptyArea;
    private Long rentPrice;
    private String serviceFee;
    private String brokerageFee;
    private String uploadfile;

}
