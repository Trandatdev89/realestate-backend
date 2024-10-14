package com.project01.reactspring.respository.CustomRepository;


import com.project01.reactspring.dto.request.BuildingRequestDTO;
import com.project01.reactspring.dto.response.BuildingResponseDTO;
import com.project01.reactspring.entity.BuildingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BuildingRepositoryCustom {
    Page<BuildingEntity> searchBuilding(BuildingRequestDTO buildingRequestDTO, Pageable pageable);
}
