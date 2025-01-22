package com.project01.reactspring.services;


import com.project01.reactspring.dto.request.*;
import com.project01.reactspring.dto.response.*;

import java.util.List;

public interface UserServices {
    AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest);
    IntrospecResponse introspecToken(TokenRequest tokenRequest);
    List<UserResponseDTO> getAllUsers();
    Void logout(TokenRequest tokenRequest);
    AuthenticateResponse refreshToken(TokenRequest tokenRequest);
    UserResponseDTO register(RegisterRequest registerRequest);
    List<DistrictOrStaffDTO> getAllStaffs();
    UserResponseDTO getMyInfo();
    UserResponseDTO getUser(Long id);
    void updateUser(Long id,UserRequestDTO userRequestDTO);
    UserResponseDTO getUserByNameOrUsername(String username);
    void updateMyInfo(Long customerId, MyInfoRequest myInfoRequest);
    AuthenticateResponse outboundAuthenticate(String code);
}
