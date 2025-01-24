package com.project01.reactspring.services.impl;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.project01.reactspring.convertor.UserDTOConvertor;
import com.project01.reactspring.dto.request.*;
import com.project01.reactspring.dto.response.*;
import com.project01.reactspring.entity.InValidateToken;
import com.project01.reactspring.entity.RoleEntity;
import com.project01.reactspring.entity.UserEntity;
import com.project01.reactspring.exception.CustomException.AppException;
import com.project01.reactspring.exception.CustomException.ErrorCode;
import com.project01.reactspring.respository.InvalidateTokenRepository;
import com.project01.reactspring.respository.RoleRepository;
import com.project01.reactspring.respository.UserRepository;
import com.project01.reactspring.services.UserServices;
import com.project01.reactspring.utils.GoogleUtil;
import com.project01.reactspring.utils.SecurityUtil;
import com.project01.reactspring.utils.UploadFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServicesImpl implements UserServices {

    private static final Logger log = LoggerFactory.getLogger(UserServicesImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserDTOConvertor userDTOConvertor;

    @Autowired
    private InvalidateTokenRepository invalidateTokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UploadFile uploadFile;

    @Autowired
    private GoogleUtil googleUtil;

    @Value("${outbound.identity.client-id}")
    @NonFinal
    private String clientId;

    @Value("${outbound.identity.client-secret}")
    @NonFinal
    private String clientSecret;


    @Value("${outbound.identity.redirect-uri}")
    @NonFinal
    private String redirectUri;

    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest) {
       UserEntity user = userRepository.findByUsername(authenticateRequest.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOTFOUND));
       if(!passwordEncoder.matches(authenticateRequest.getPassword(), user.getPassword())) {
           throw new AppException(ErrorCode.PASSWORD_WRONG);
       }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(),authenticateRequest.getPassword());
        AuthenticationManager authenticationManager=authenticationManagerBuilder.getObject();
        Authentication authentication=authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        String token = securityUtil.generateToken(user);
        return AuthenticateResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospecResponse introspecToken(TokenRequest tokenRequest){
        boolean isvalid= false;
        try {
            securityUtil.verifyToken(tokenRequest.getToken());
            isvalid = true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return IntrospecResponse.builder()
                .valid(isvalid)
                .build();
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        for(UserEntity item : users){
            UserResponseDTO userResponseDTO=userDTOConvertor.toUserDTOResponse(item);
            userResponseDTOS.add(userResponseDTO);
        }
        return userResponseDTOS;
    }

    @Override
    public Void logout(TokenRequest tokenRequest) {
        try {
            SignedJWT signedJWT = securityUtil.verifyToken(tokenRequest.getToken());
            Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
            System.out.println(expirationDate.after(new Date()));
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            String username = signedJWT.getJWTClaimsSet().getSubject();
            UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

            InValidateToken inValidateToken = InValidateToken.builder()
                    .id(jwtId)
                    .expirytime(expirationDate)
                    .user(user)
                    .build();

            invalidateTokenRepository.save(inValidateToken);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public AuthenticateResponse refreshToken(TokenRequest tokenRequest) {
        try {
            SignedJWT signedJWT=securityUtil.verifyToken(tokenRequest.getToken());
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            String username = signedJWT.getJWTClaimsSet().getSubject();
            Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
            UserEntity user=userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

            InValidateToken inValidateToken = InValidateToken.builder()
                    .id(jwtId)
                    .expirytime(expirationDate)
                    .user(user)
                    .build();
            invalidateTokenRepository.save(inValidateToken);

            String token = securityUtil.generateToken(user);
            return AuthenticateResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponseDTO register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new AppException(ErrorCode.USER_EXITSED);
        }
        List<Long> roleId = new ArrayList<>();
        roleId.add(3L);
        UserEntity userEntity=UserEntity.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .address(registerRequest.getAddress())
                .birth(registerRequest.getBirth())
                .fullname(registerRequest.getFullname())
                .roles(roleRepository.findAllById(roleId))
                .build();

        UserEntity userRegister=userRepository.save(userEntity);

        UserResponseDTO userResponseDTO=userDTOConvertor.toUserDTOResponse(userRegister);
        return userResponseDTO;
    }

    @Override
    public List<DistrictOrStaffDTO> getAllStaffs() {
         List<UserEntity> users = userRepository.findByRoles_Code("STAFF");
         List<DistrictOrStaffDTO> districtOrStaffDTOS = new ArrayList<>();
         for(UserEntity item : users){
             districtOrStaffDTOS.add(DistrictOrStaffDTO.builder().label(item.getFullname()).value(item.getId()).build());
         }
         return districtOrStaffDTOS;
    }

    @Override
    public UserResponseDTO getMyInfo() {
        String name= SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user=userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        UserResponseDTO userResponseDTO=userDTOConvertor.toUserDTOResponse(user);

        return userResponseDTO;
    }

    @Override
    public UserResponseDTO getUser(Long id) {
        UserEntity user=userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        UserResponseDTO userResponseDTO=userDTOConvertor.toUserDTOResponse(user);
        return userResponseDTO;
    }

    @Override
    public void updateUser(Long id, UserRequestDTO userRequestDTO) {
        UserEntity user=userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        List<Long> longList = userRequestDTO.getRole().stream().map(Long::valueOf).collect(Collectors.toList());
        List<RoleEntity> roles = roleRepository.findAllById(longList);
        user.setRoles(roles);
        userRepository.save(user);
    }


    @Override
    public UserResponseDTO getUserByNameOrUsername(String username) {
        try {
            UserEntity pageUser=userRepository.findByUsernameContainingIgnoreCase(username);
            UserResponseDTO userResponseDTO=userDTOConvertor.toUserDTOResponse(pageUser);
            return userResponseDTO;
        }catch (IllegalArgumentException exception){
            throw new AppException(ErrorCode.SEARCH_USERNAME);
        }
    }

    @Override
    public void updateMyInfo(Long customerId, MyInfoRequest myInfoRequest) {
        UserEntity user=userRepository.findById(customerId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        if(myInfoRequest.getThumnail()!=null){
            String thumnail= null;
            try {
                thumnail = uploadFile.uploadFile(myInfoRequest.getThumnail());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user.setThumnail(thumnail);
        }
        user.setAddress(myInfoRequest.getAddress());
        user.setFullname(myInfoRequest.getFullname());
        userRepository.save(user);
    }

    @Override
    public AuthenticateResponse outboundAuthenticate(String code ) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token", requestEntity, Map.class);

        String accessToken = (String) response.getBody().get("access_token");
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);

        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);
        ResponseEntity<Map> userResponse = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                userRequest,
                Map.class);

        Map<String, Object> userInfo = userResponse.getBody();
        GoogleDTO googleDTO = GoogleDTO.builder()
                .googleId((String) userInfo.get("id"))
                .email((String) userInfo.get("email"))
                .thumnail((String) userInfo.get("picture"))
                .username((String) userInfo.get("email"))
                .fullname((String) userInfo.get("name"))
                .build();

        return AuthenticateResponse.builder()
                .token(googleUtil.generateTokenGoogle(googleDTO))
                .authenticated(true)
                .build();
    }
}
