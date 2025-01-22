package com.project01.reactspring.convertor;

import com.project01.reactspring.dto.response.UserResponseDTO;
import com.project01.reactspring.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDTOConvertor {

    @Autowired
    private ModelMapper modelMapper;

    public UserResponseDTO toUserDTOResponse(UserEntity item){
        UserResponseDTO userResponseDTO=modelMapper.map(item, UserResponseDTO.class);
        List<String> role=item.getRoles().stream().map(it->it.getCode().toString()).collect(Collectors.toList());
        userResponseDTO.setRole(role);
        String thumnail;
        if(item.getGoogleid()!=null){
            thumnail=item.getThumnail();
        }else{
            thumnail= ServletUriComponentsBuilder.fromCurrentContextPath().toUriString()+"/avatar/"+item.getThumnail();
        }
        userResponseDTO.setThumnail(thumnail);
        return userResponseDTO;
    }
}
