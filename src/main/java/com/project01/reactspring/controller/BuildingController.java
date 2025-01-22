package com.project01.reactspring.controller;


import com.project01.reactspring.dto.request.AssignmentRequest;
import com.project01.reactspring.dto.request.BuildingRequestDTO;
import com.project01.reactspring.dto.request.BuildingRequestForm;
import com.project01.reactspring.dto.response.*;
import com.project01.reactspring.services.BuildingServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/building")
public class BuildingController {

    @Autowired
    private BuildingServices buildingServices;


    @GetMapping
    public ApiResponse<PaginationDTO> getAllBuildings(@RequestParam(required = false) String username,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "6") int size) {
        PaginationDTO result=buildingServices.findAllBuilding(username,page,size);
        return ApiResponse.<PaginationDTO>builder()
                .data(result)
                .build();
    }

    @PostMapping("/search")
    public ApiResponse<PaginationDTO> searchBuilding(@RequestParam(required = false) String username,@RequestBody BuildingRequestDTO buildingRequestDTO,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "6") int size) {
        PaginationDTO result= buildingServices.search(username,buildingRequestDTO,page,size);
        return ApiResponse.<PaginationDTO>builder()
                  .data(result)
                  .build();
    }

    //Get thông tin sửa nhà
    @GetMapping("/info/{id}")
    public ApiResponse<BuildingRequestForm> getInfoBuilding(@PathVariable Long id) {
        BuildingRequestForm result= buildingServices.getInfoBuilding(id);
        return ApiResponse.<BuildingRequestForm>builder()
                .data(result)
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse updateBuilding(@PathVariable Long id,@ModelAttribute BuildingRequestForm buildingRequestForm) {
        buildingServices.updateBuilding(id,buildingRequestForm);
        return ApiResponse.builder()
                .code(200)
                .message("success")
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse createBuilding(@ModelAttribute  BuildingRequestForm buildingRequestForm) {
        buildingServices.createBuilding(buildingRequestForm);
        return ApiResponse.builder()
                .code(200)
                .message("success")
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse deleteBuilding(@PathVariable Long[] id) {
        buildingServices.deleteBuilding(id);
        return ApiResponse.builder()
                .code(200)
                .message("success")
                .build();
    }

    @PostMapping("/assignment")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse assignmentBuilding(@RequestBody AssignmentRequest assignmentRequest) {
        buildingServices.assignmentBuilding(assignmentRequest);
        return ApiResponse.builder()
                .code(200)
                .message("success")
                .build();
    }

    @GetMapping("/{buildingId}/staff")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<StaffResponseDTO>> getInfoAssignment(@PathVariable Long buildingId) {
        List<StaffResponseDTO> result=buildingServices.managerBuilding(buildingId);
        return  ApiResponse.<List<StaffResponseDTO>>builder()
                .data(result)
                .build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<List<BuildingRequestForm>> getAllBuilding() {
        List<BuildingRequestForm> result=buildingServices.getAllBuilding();
        return  ApiResponse.<List<BuildingRequestForm>>builder()
                .data(result)
                .build();
    }


}
