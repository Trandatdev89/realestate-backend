package com.project01.reactspring.convertor;

import com.project01.reactspring.dto.request.BuildingRequestForm;
import com.project01.reactspring.dto.response.BuildingResponseDTO;
import com.project01.reactspring.entity.BuildingEntity;
import com.project01.reactspring.entity.RentAreaEntity;
import com.project01.reactspring.respository.RentAreaRepository;
import com.project01.reactspring.utils.UploadFile;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class BuildingDTOConvertor {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UploadFile uploadFile;


    @Autowired
    private RentAreaRepository rentAreaRepository;

    public BuildingEntity toBuildingEntity(BuildingRequestForm buildingRequestForm){
        BuildingEntity buildingEntity = modelMapper.map(buildingRequestForm, BuildingEntity.class);
        String typeCodeConvert=buildingRequestForm.getTypeCode().stream().map(item->item.toString()).collect(Collectors.joining(","));
        if(buildingRequestForm.getUploadfile()!=null){
            String uploadfile= null;
            try {
                uploadfile = uploadFile.uploadFile(buildingRequestForm.getUploadfile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            buildingEntity.setUploadfile(uploadfile);
        }
        List<String> rentAreaEntities= Arrays.asList(buildingRequestForm.getRentAreaString().split(","));
        List<RentAreaEntity> rentAreaEntityList=new ArrayList<>();
        for(String item: rentAreaEntities){
            RentAreaEntity rentAreaEntity=RentAreaEntity.builder()
                    .value(Long.parseLong(item))
                    .building(buildingEntity)
                    .build();
            rentAreaEntityList.add(rentAreaEntity);
        }

        buildingEntity.setType(typeCodeConvert);
        buildingEntity.setRentarea(rentAreaEntityList);
        return buildingEntity;
    }

    public BuildingRequestForm toBuildingRequestForm(BuildingEntity buildingEntity){
            BuildingRequestForm buildingRequestForm = modelMapper.map(buildingEntity, BuildingRequestForm.class);
            List<String> typeCodeConvert = Arrays.asList(buildingEntity.getType().split(","));
            List<RentAreaEntity> rentAreaEntityList=buildingEntity.getRentarea();
            String rentAreaConvert=rentAreaEntityList.stream().map(item->item.getValue().toString()).collect(Collectors.joining(","));
            buildingRequestForm.setUploadfileString(buildingEntity.getUploadfile());
            buildingRequestForm.setTypeCode(typeCodeConvert);
            buildingRequestForm.setRentAreaString(rentAreaConvert);


        return buildingRequestForm;
    }

    public BuildingEntity toBuildingEntityUpdate(BuildingRequestForm buildingRequestForm,BuildingEntity buildingEntity){
        if(buildingEntity.getId()!=null){
            rentAreaRepository.deleteByBuilding_Id(buildingEntity.getId());
        }

        String typeCodeConvert=buildingRequestForm.getTypeCode().stream().map(item->item.toString()).collect(Collectors.joining(","));
        String uploadfile= null;
        try {
            uploadfile = uploadFile.uploadFile(buildingRequestForm.getUploadfile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> rentAreaEntities= Arrays.asList(buildingRequestForm.getRentAreaString().split(","));

        List<RentAreaEntity> rentAreaEntityList=new ArrayList<>();
        for(String item: rentAreaEntities){
            RentAreaEntity rentAreaEntity=RentAreaEntity.builder()
                    .value(Long.parseLong(item))
                    .building(buildingEntity)
                    .build();
            rentAreaEntityList.add(rentAreaEntity);
        }
        buildingEntity.setUploadfile(uploadfile);
        buildingEntity.setType(typeCodeConvert);
        buildingEntity.setRentarea(rentAreaEntityList);
        return buildingEntity;
    }

    public BuildingResponseDTO toBuildingResponseDTO(BuildingEntity buildingEntity){
        BuildingResponseDTO buildingResponseDTO = modelMapper.map(buildingEntity, BuildingResponseDTO.class);
        String address=buildingEntity.getDistrict()+" "+buildingEntity.getStreet()+" "+buildingEntity.getWard();
        List<RentAreaEntity> rentAreaEntityList=buildingEntity.getRentarea();
        String rentArea=rentAreaEntityList.stream().map(item->item.getValue().toString()).collect(Collectors.joining(","));
        buildingResponseDTO.setUploadfile(buildingEntity.getUploadfile());
        buildingResponseDTO.setAddress(address);
        buildingResponseDTO.setRentAreaString(rentArea);
        return buildingResponseDTO;
    }

}
