package com.project01.reactspring.respository;

import com.project01.reactspring.entity.RentAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentAreaRepository extends JpaRepository<RentAreaEntity,Long> {
    void deleteByBuilding_Id(Long buildingid);
}
