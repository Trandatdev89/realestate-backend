package com.project01.reactspring.controller;


import com.project01.reactspring.dto.request.*;
import com.project01.reactspring.dto.response.ApiResponse;
import com.project01.reactspring.dto.response.AuthenticateResponse;
import com.project01.reactspring.dto.response.IntrospecResponse;
import com.project01.reactspring.dto.response.UserResponseDTO;
import com.project01.reactspring.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/login")
    public ApiResponse<AuthenticateResponse> login(@RequestBody @Valid AuthenticateRequest authenticateRequest) {
        return ApiResponse.<AuthenticateResponse>builder()
                .code(200)
                .message("success!")
                .data(userServices.authenticate(authenticateRequest))
                .build();
    }

    @PostMapping("/introspectoken")
    public ApiResponse<IntrospecResponse> IntrospecToken(@RequestBody @Valid TokenRequest tokenRequest) {
        return ApiResponse.<IntrospecResponse>builder()
                .code(200)
                .message("success!")
                .data(userServices.introspecToken(tokenRequest))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid TokenRequest tokenRequest) {
        return ApiResponse.<Void>builder()
                .code(200)
                .message("success!")
                .data(userServices.logout(tokenRequest))
                .build();
    }

    @PostMapping("/resfresh-token")
    public ApiResponse<AuthenticateResponse> ResfreshToken(@RequestBody @Valid TokenRequest tokenRequest) {
        return ApiResponse.<AuthenticateResponse>builder()
                .code(200)
                .message("success!")
                .data(userServices.refreshToken(tokenRequest))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<UserResponseDTO> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return ApiResponse.<UserResponseDTO>builder()
                .code(200)
                .message("success!")
                .data(userServices.register(registerRequest))
                .build();
    }
}
