package com.project01.reactspring.respository;

import com.project01.reactspring.dto.response.CustomerResponseDTO;
import com.project01.reactspring.entity.CustomerEntity;
import com.project01.reactspring.respository.CustomRepository.CustomerRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>, CustomerRepositoryCustom{
    Page<CustomerEntity> findAllByStatus(Pageable pageable,boolean status);
    CustomerEntity findByPhone(String phone);
    boolean existsByPhone(String phone);
    Page<CustomerEntity> findAllByUsers_IdAndStatus(Pageable pageable,Long id,boolean status);
}
