package com.project01.reactspring.respository;

import com.project01.reactspring.dto.request.BuildingRequestDTO;
import com.project01.reactspring.dto.response.BuildingResponseDTO;
import com.project01.reactspring.entity.BuildingEntity;
import com.project01.reactspring.respository.CustomRepository.BuildingRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Long>, BuildingRepositoryCustom{
    void deleteByIdIn(Long[] id);
    Page<BuildingEntity> findAll(Pageable pageable);
    Page<BuildingEntity> findAllByUsers_Id(Pageable pageable, Long id);
}
