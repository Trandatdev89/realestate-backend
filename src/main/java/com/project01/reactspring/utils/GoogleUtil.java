package com.project01.reactspring.utils;

import com.project01.reactspring.dto.request.GoogleDTO;
import com.project01.reactspring.entity.UserEntity;
import com.project01.reactspring.exception.CustomException.AppException;
import com.project01.reactspring.exception.CustomException.ErrorCode;
import com.project01.reactspring.respository.RoleRepository;
import com.project01.reactspring.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GoogleUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SecurityUtil securityUtil;


    public String generateTokenGoogle(GoogleDTO googleDTO) {
        if (!userRepository.existsByGoogleid(googleDTO.getGoogleId())) {
            UserEntity user = UserEntity.builder()
                    .username(googleDTO.getUsername())
                    .googleid(googleDTO.getGoogleId())
                    .fullname(googleDTO.getFullname())
                    .email(googleDTO.getEmail())
                    .thumnail(googleDTO.getThumnail())
                    .build();

            user.setRoles(List.of(roleRepository.findById(3L).get()));
            userRepository.save(user);

        }
        UserEntity user = userRepository.findByGoogleid(googleDTO.getGoogleId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", googleDTO.getUsername());
        attributes.put("email", googleDTO.getEmail());

        OAuth2User oAuth2User = new DefaultOAuth2User(user.getAuthorities(), attributes, "email");
        OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(
                oAuth2User, user.getAuthorities(), "google");

        SecurityContextHolder.getContext().setAuthentication(oAuth2AuthenticationToken);

        String token = securityUtil.generateToken(user);
        return token;
    }
}