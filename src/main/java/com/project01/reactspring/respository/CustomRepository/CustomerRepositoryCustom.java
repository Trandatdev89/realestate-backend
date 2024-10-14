package com.project01.reactspring.respository.CustomRepository;

import com.project01.reactspring.dto.response.CustomerResponseDTO;
import com.project01.reactspring.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {
    Page<CustomerEntity> search(CustomerResponseDTO customerResponseDTO, Pageable pageable) throws IllegalAccessException;
}
