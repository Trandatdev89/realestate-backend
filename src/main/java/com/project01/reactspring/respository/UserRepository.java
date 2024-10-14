package com.project01.reactspring.respository;

import com.project01.reactspring.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<UserEntity> findByBuilding_Id(Long buildingId);
    List<UserEntity> findByRoles_Code(String code);
    UserEntity findByUsernameContainingIgnoreCase(String username);
}
