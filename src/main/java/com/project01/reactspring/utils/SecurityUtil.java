package com.project01.reactspring.utils;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.SignedJWT;
import com.project01.reactspring.entity.UserEntity;
import com.project01.reactspring.exception.CustomException.AppException;
import com.project01.reactspring.exception.CustomException.ErrorCode;
import com.project01.reactspring.respository.InvalidateTokenRepository;
import com.project01.reactspring.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SecurityUtil {

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Autowired
    @Lazy
    private JwtEncoder jwtEncoder;

    @Autowired
    private InvalidateTokenRepository invalidateTokenRepository;

    public static final MacAlgorithm MAC_ALGORITHM=MacAlgorithm.HS256;


    public String generateToken(UserEntity user){
        JwsHeader jwsHeader=JwsHeader.with(MAC_ALGORITHM).build();
        Instant now=Instant.now();

        Instant expiresAt=now.plusSeconds(expiration);
        JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getUsername())
                .claim("scope",getAuthority((List<GrantedAuthority>) user.getAuthorities()))
                .id(UUID.randomUUID().toString())
                .build();

        String token=jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,jwtClaimsSet)).getTokenValue();
        return token;
    }

    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT=SignedJWT.parse(token);
        JWSVerifier jwsVerifier=new MACVerifier(getSecretKey());
        boolean verified=signedJWT.verify(jwsVerifier);
        if(!(verified && signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date()))){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        if((invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return signedJWT;
    }

    public SecretKey getSecretKey() {
        byte[] bytes= Base64.from(secretKey).decode();
        return new SecretKeySpec(bytes,0,bytes.length,SecurityUtil.MAC_ALGORITHM.getName());
    }

    public String getAuthority(List<GrantedAuthority> roles){
        String authority=roles.stream().map(it->it.getAuthority().toString().substring(5)).collect(Collectors.joining(" "));
        return authority;
    }
}
