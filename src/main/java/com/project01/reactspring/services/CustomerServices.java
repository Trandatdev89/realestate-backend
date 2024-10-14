package com.project01.reactspring.services;


import com.project01.reactspring.dto.request.AssignmentRequest;
import com.project01.reactspring.dto.response.CustomerResponseDTO;
import com.project01.reactspring.dto.response.PaginationDTO;
import com.project01.reactspring.dto.response.StaffResponseDTO;

import java.util.List;

public interface CustomerServices {
    PaginationDTO<CustomerResponseDTO> findAllCustomer(int page, int size);
    PaginationDTO<CustomerResponseDTO> searchCustomer(CustomerResponseDTO customerResponseDTO,int page, int size) throws IllegalAccessException;
    CustomerResponseDTO addCustomer(CustomerResponseDTO customerResponseDTO);
    void updateCustomer(Long id,CustomerResponseDTO customerResponseDTO);
    void deleteCustomer(Long id);
    void assignmentCustomer( AssignmentRequest assignmentRequest);
    List<StaffResponseDTO> getInfoAssignment(Long customerId);
    CustomerResponseDTO getInfoCustomer(Long customerId);
//    PaginationDTO<CustomerResponseDTO> searchAllCustomerByStaff(int page,int size);
}
