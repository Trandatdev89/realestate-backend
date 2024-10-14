package com.project01.reactspring.services;

import com.project01.reactspring.dto.request.AssignmentRequest;
import com.project01.reactspring.dto.request.BuildingRequestDTO;
import com.project01.reactspring.dto.request.BuildingRequestForm;
import com.project01.reactspring.dto.response.BuildingResponseDTO;
import com.project01.reactspring.dto.response.PaginationDTO;
import com.project01.reactspring.dto.response.StaffResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BuildingServices {
    PaginationDTO findAllBuilding(String username,int page,int size);
    PaginationDTO search(String username,BuildingRequestDTO buildingRequestDTO,int page,int size);
    void createBuilding(BuildingRequestForm buildingRequestForm);
    BuildingRequestForm getInfoBuilding(Long id);
    void updateBuilding(Long id,BuildingRequestForm buildingRequestForm);
    void deleteBuilding(Long[] id);
    void assignmentBuilding(AssignmentRequest assignmentRequest);
    List<StaffResponseDTO> managerBuilding(Long id);
}

