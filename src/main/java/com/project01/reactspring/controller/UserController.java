package com.project01.reactspring.controller;


import com.project01.reactspring.dto.request.UserRequestDTO;
import com.project01.reactspring.dto.response.ApiResponse;
import com.project01.reactspring.dto.response.DistrictOrStaffDTO;
import com.project01.reactspring.dto.response.UserResponseDTO;
import com.project01.reactspring.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServices userServices;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<List<UserResponseDTO>> getAllUsers() {
       return ApiResponse.<List<UserResponseDTO>>builder()
               .code(200)
               .message("success!")
               .data(userServices.getAllUsers())
               .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<UserResponseDTO> getUser(@PathVariable Long id) {
        return ApiResponse.<UserResponseDTO>builder()
                .code(200)
                .message("success!")
                .data(userServices.getUser(id))
                .build();
    }

    @GetMapping("/staff")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<DistrictOrStaffDTO> getAllStaffs() {
        return userServices.getAllStaffs();
    }

    @GetMapping("/my-info")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<UserResponseDTO> getMyInfo() {
        return ApiResponse.<UserResponseDTO>builder()
                .code(200)
                .message("success!")
                .data(userServices.getMyInfo())
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse updateUser(@PathVariable Long id,@RequestBody UserRequestDTO userRequestDTO) {
        userServices.updateUser(id,userRequestDTO);
        return ApiResponse.builder()
                .code(200)
                .message("success!")
                .build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse<UserResponseDTO> getUserByNameOrUsername(@RequestParam String username) {
        return ApiResponse.<UserResponseDTO>builder()
                .code(200)
                .message("success!")
                .data(userServices.getUserByNameOrUsername(username))
                .build();
    }

}
