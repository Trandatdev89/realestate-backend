package com.project01.reactspring.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildingRequestForm {
    private Long id;
    private String name;
    private String street;
    private String ward;
    private String district;
    private String structure;
    private Long numberofbasement;
    private Long floorarea;
    private String direction;
    private String level;
    private Long rentprice;
    private String rentAreaString;
    private String servicefee;
    private String brokeragefee;
    private List<String> typeCode=new ArrayList<>();
    private String note;
    private String managername;
    private String managerphone;
    private String demand;
    private MultipartFile uploadfile;
    private String uploadfileString;
}
