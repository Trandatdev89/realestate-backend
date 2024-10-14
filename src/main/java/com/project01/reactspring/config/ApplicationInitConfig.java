package com.project01.reactspring.config;

import com.project01.reactspring.entity.RoleEntity;
import com.project01.reactspring.entity.UserEntity;
import com.project01.reactspring.respository.RoleRepository;
import com.project01.reactspring.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            Optional<UserEntity> user = userRepository.findByUsername("admin");
            if (user.isEmpty()) {
                List<RoleEntity> role = new ArrayList<>();
                role.add(roleRepository.findById(1L).get());
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .address("Hung Yen")
                        .id(1L)
                        .birth(new Date())
                        .fullname("Tran Quoc Dat")
                        .building(null)
                        .roles(role)
                        .build();
                userRepository.save(admin);
            } else {
                log.info("Admin exits already !");
            }
        };

    }
}
