package com.project01.reactspring.controller;


import com.project01.reactspring.dto.request.AssignmentRequest;
import com.project01.reactspring.dto.request.BuildingRequestForm;
import com.project01.reactspring.dto.response.ApiResponse;
import com.project01.reactspring.dto.response.CustomerResponseDTO;
import com.project01.reactspring.dto.response.PaginationDTO;
import com.project01.reactspring.dto.response.StaffResponseDTO;
import com.project01.reactspring.services.CustomerServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerServices customerServices;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<PaginationDTO> getAllCustomer(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        PaginationDTO result=customerServices.findAllCustomer(page,size);
        return ApiResponse.<PaginationDTO>builder()
                .data(result)
                .build();
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<PaginationDTO> searchCustomer(@RequestBody CustomerResponseDTO customerResponseDTO,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) throws IllegalAccessException {
        PaginationDTO result=customerServices.searchCustomer(customerResponseDTO,page,size);
        return ApiResponse.<PaginationDTO>builder()
                .data(result)
                .build();
    }

    @PostMapping
    public ApiResponse<CustomerResponseDTO> addCustomer(@RequestBody @Valid CustomerResponseDTO customerResponseDTO) {
        return ApiResponse.<CustomerResponseDTO>builder()
                .code(200)
                .data(customerServices.addCustomer(customerResponseDTO))
                .message("success")
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse updateCustomer(@PathVariable Long id,@RequestBody CustomerResponseDTO customerResponseDTO) {
        customerServices.updateCustomer(id,customerResponseDTO);
        return ApiResponse.builder()
                .code(200)
                .message("success")
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse deleteCustomer(@PathVariable Long id) {
        customerServices.deleteCustomer(id);
        return ApiResponse.builder()
                .code(200)
                .message("success")
                .build();
    }

    @PostMapping("/assignmentcustomer")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse assignmentCustomer(@RequestBody AssignmentRequest assignmentRequest) {
        customerServices.assignmentCustomer(assignmentRequest);
        return ApiResponse.builder()
                .code(200)
                .message("success")
                .build();
    }

    @GetMapping("/{customerid}/staff")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<StaffResponseDTO>> getInfoAssignment(@PathVariable Long customerid) {
        List<StaffResponseDTO> result=customerServices.getInfoAssignment(customerid);
        return  ApiResponse.<List<StaffResponseDTO>>builder()
                .data(result)
                .build();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<CustomerResponseDTO> getInfoCustomer(@PathVariable Long id) {
        CustomerResponseDTO result= customerServices.getInfoCustomer(id);
        return ApiResponse.<CustomerResponseDTO>builder()
                .data(result)
                .build();
    }


//    @GetMapping("/searchstaff")
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
//    public ApiResponse<PaginationDTO> searchAllCustomerByStaff(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
//        PaginationDTO result= customerServices.searchAllCustomerByStaff(page,size);
//        return ApiResponse.<PaginationDTO>builder()
//                .data(result)
//                .build();
//    }
}
