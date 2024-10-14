package com.project01.reactspring.services.impl;


import com.project01.reactspring.convertor.BuildingDTOConvertor;
import com.project01.reactspring.dto.request.AssignmentRequest;
import com.project01.reactspring.dto.request.BuildingRequestDTO;
import com.project01.reactspring.dto.request.BuildingRequestForm;
import com.project01.reactspring.dto.response.BuildingResponseDTO;
import com.project01.reactspring.dto.response.PaginationDTO;
import com.project01.reactspring.dto.response.StaffResponseDTO;
import com.project01.reactspring.entity.BuildingEntity;
import com.project01.reactspring.entity.RentAreaEntity;
import com.project01.reactspring.entity.UserEntity;
import com.project01.reactspring.exception.CustomException.AppException;
import com.project01.reactspring.exception.CustomException.ErrorCode;
import com.project01.reactspring.respository.BuildingRepository;
import com.project01.reactspring.respository.UserRepository;
import com.project01.reactspring.services.BuildingServices;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Transactional
public class BuildingServicesImpl implements BuildingServices {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingDTOConvertor buildingDTOConvertor;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PaginationDTO<BuildingResponseDTO> findAllBuilding(String username,int page,int size) {
        List<BuildingResponseDTO> buildingResponseDTOS = new ArrayList<>();
        Pageable pageable= PageRequest.of(page,size);

        Page<BuildingEntity> pageBuilding=null;
        if(username!=null && !username.equals("")){
            UserEntity user=userRepository.findByUsername(username).get();
            List<String> role=user.getRoles().stream().map(item->item.getCode().toString()).toList();
            if(role.contains("STAFF")){
                pageBuilding= buildingRepository.findAllByUsers_Id(pageable,user.getId());
            }
            else{
                pageBuilding= buildingRepository.findAll(pageable);
            }
        }
        else{
             pageBuilding= buildingRepository.findAll(pageable);
        }

        for(BuildingEntity buildingEntity : pageBuilding.getContent()) {
            BuildingResponseDTO buildingResponseDTO=buildingDTOConvertor.toBuildingResponseDTO(buildingEntity);
            buildingResponseDTOS.add(buildingResponseDTO);
        }

        PaginationDTO paginationDTO = PaginationDTO.<BuildingResponseDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .data(buildingResponseDTOS)
                .totalItem(pageBuilding.getTotalElements())
                .totalPages(pageBuilding.getTotalPages())
                .build();
        return paginationDTO;
    }

    @Override
    public PaginationDTO<BuildingResponseDTO> search(String username,BuildingRequestDTO buildingRequestDTO,int page,int size) {
        Pageable pageable= PageRequest.of(page,size);

        if(username!=null && !username.equals("")){
            UserEntity user=userRepository.findByUsername(username).get();
            List<String> role=user.getRoles().stream().map(item->item.getCode().toString()).toList();

            if(role.contains("STAFF")){
                buildingRequestDTO.setStaffId(user.getId());
            }
        }

        Page<BuildingEntity> pageBuilding = buildingRepository.searchBuilding(buildingRequestDTO,pageable);
        List<BuildingResponseDTO> buildingResponseDTOS = new ArrayList<>();

        for(BuildingEntity buildingEntity : pageBuilding.getContent()) {
            BuildingResponseDTO buildingResponseDTO=buildingDTOConvertor.toBuildingResponseDTO(buildingEntity);
            buildingResponseDTOS.add(buildingResponseDTO);
        }

        PaginationDTO paginationDTO = PaginationDTO.<BuildingResponseDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .data(buildingResponseDTOS)
                .totalItem(pageBuilding.getTotalElements())
                .totalPages(pageBuilding.getTotalPages())
                .build();
        return paginationDTO;
    }

    @Override
    public void createBuilding(BuildingRequestForm buildingRequestForm) {
        BuildingEntity buildingEntity=buildingDTOConvertor.toBuildingEntity(buildingRequestForm);
        buildingRepository.save(buildingEntity);
    }

    @Override
    public BuildingRequestForm getInfoBuilding(Long id) {
        BuildingEntity building=buildingRepository.findById(id).get();
        BuildingRequestForm buildingRequestForm=buildingDTOConvertor.toBuildingRequestForm(building);
        return buildingRequestForm;
    }

    @Override
    public void updateBuilding(Long id, BuildingRequestForm buildingRequestForm) {
        BuildingEntity building1=buildingRepository.findById(id).get();
        BuildingEntity building2=modelMapper.map(buildingRequestForm,BuildingEntity.class);
        building2.setUsers(building1.getUsers());
        buildingRepository.save(buildingDTOConvertor.toBuildingEntityUpdate(buildingRequestForm,building2));
    }

    @Override
    public void deleteBuilding(Long[] id) {
        buildingRepository.deleteByIdIn(id);
    }

    @Override
    public void assignmentBuilding(AssignmentRequest assignmentRequest) {
        BuildingEntity building=buildingRepository.findById(assignmentRequest.getBuildingId()).get();
        List<UserEntity> userEntities = new ArrayList<>();
        List<Long> staffId=assignmentRequest.getStaffId();
        for(Long item:staffId){
            UserEntity user=userRepository.findById(item).get();
            userEntities.add(user);
        }
        building.setUsers(userEntities);
        buildingRepository.save(building);
    }

    @Override
    public List<StaffResponseDTO> managerBuilding(Long id) {
        List<StaffResponseDTO> staffResponseDTO=new ArrayList<>();
        BuildingEntity building=buildingRepository.findById(id).get();
        List<UserEntity> userEntities=building.getUsers();
        List<UserEntity> user=userRepository.findByRoles_Code("STAFF");

        for(UserEntity item:user){
            if(userEntities.contains(item)){
                StaffResponseDTO staffResponseDTO1=StaffResponseDTO.builder()
                        .fullName(item.getFullname())
                        .staffId(item.getId())
                        .checked(true)
                        .build();
                staffResponseDTO.add(staffResponseDTO1);
            }
            else {
                StaffResponseDTO staffResponseDTO1=StaffResponseDTO.builder()
                        .fullName(item.getFullname())
                        .staffId(item.getId())
                        .checked(false)
                        .build();
                staffResponseDTO.add(staffResponseDTO1);
            }
        }
        return staffResponseDTO;
    }
}
