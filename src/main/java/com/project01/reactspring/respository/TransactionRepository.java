package com.project01.reactspring.respository;

import com.project01.reactspring.dto.response.TransactionReponseDTO;
import com.project01.reactspring.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByCustomer_Id(Long id);
}
