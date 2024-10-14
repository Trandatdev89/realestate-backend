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
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            List<RoleEntity> roleEntity=roleRepository.findAll();
            if(roleEntity.size()==0){
                List<RoleEntity> roles= new ArrayList<>();
                RoleEntity role1=RoleEntity.builder()
                        .code("ADMIN")
                        .name("admin").build();
                RoleEntity role2=RoleEntity.builder()
                        .code("STAFF")
                        .name("nhanvien").build();
                RoleEntity role3=RoleEntity.builder()
                        .code("USER")
                        .name("khach").build();
                roles.add(role1);
                roles.add(role2);
                roles.add(role3);
                roleRepository.saveAll(roles);
            }
            else{
                boolean user = userRepository.existsByUsername("admin");
                if(user==false){
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
                }
                else{
                    log.info("Admin Exits");
                }
            }
        };
    }
}
