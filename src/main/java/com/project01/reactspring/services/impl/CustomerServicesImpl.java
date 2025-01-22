package com.project01.reactspring.services.impl;

import com.project01.reactspring.dto.request.AssignmentRequest;
import com.project01.reactspring.dto.request.MyInfoRequest;
import com.project01.reactspring.dto.response.CustomerResponseDTO;
import com.project01.reactspring.dto.response.PaginationDTO;
import com.project01.reactspring.dto.response.StaffResponseDTO;
import com.project01.reactspring.entity.CustomerEntity;
import com.project01.reactspring.entity.TransactionEntity;
import com.project01.reactspring.entity.UserEntity;
import com.project01.reactspring.exception.CustomException.AppException;
import com.project01.reactspring.exception.CustomException.ErrorCode;
import com.project01.reactspring.respository.CustomerRepository;
import com.project01.reactspring.respository.TransactionRepository;
import com.project01.reactspring.respository.UserRepository;
import com.project01.reactspring.services.CustomerServices;
import com.project01.reactspring.utils.SendEmail;
import com.project01.reactspring.utils.UploadFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerServicesImpl implements CustomerServices {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmail sendEmail;

    @Override
    public PaginationDTO<CustomerResponseDTO> findAllCustomer(int page, int size) {
        List<CustomerResponseDTO> result = new ArrayList<>();
        Pageable pageable= PageRequest.of(page,size);

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user=userRepository.findByUsername(username).get();
        List<String> role=user.getRoles().stream().map(item->item.getCode().toString()).toList();

        Page<CustomerEntity> pageCustomer=null;
        if(role.contains("STAFF")){
            pageCustomer= customerRepository.findAllByUsers_IdAndStatus(pageable,user.getId(),true);
        }
        else{
            pageCustomer= customerRepository.findAllByStatus(pageable,true);
        }

        for(CustomerEntity item : pageCustomer.getContent()) {
            CustomerResponseDTO customerResponseDTO=modelMapper.map(item,CustomerResponseDTO.class);
            result.add(customerResponseDTO);
        }

        PaginationDTO paginationDTO = PaginationDTO.<CustomerResponseDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .data(result)
                .totalItem(pageCustomer.getTotalElements())
                .totalPages(pageCustomer.getTotalPages())
                .build();
        return paginationDTO;
    }

    @Override
    public PaginationDTO<CustomerResponseDTO> searchCustomer(CustomerResponseDTO customerResponseDTO, int page, int size) throws IllegalAccessException {
        List<CustomerResponseDTO> result = new ArrayList<>();
        Pageable pageable= PageRequest.of(page,size);

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user=userRepository.findByUsername(username).get();
        List<String> role=user.getRoles().stream().map(item->item.getCode().toString()).toList();

        if(role.contains("STAFF")){
            customerResponseDTO.setStaffid(user.getId());
        }
        Page<CustomerEntity> pageCustomer= customerRepository.search(customerResponseDTO,pageable);

        for(CustomerEntity item : pageCustomer.getContent()) {
            CustomerResponseDTO customerResponseDTO1=modelMapper.map(item,CustomerResponseDTO.class);
            result.add(customerResponseDTO1);
        }
        PaginationDTO paginationDTO = PaginationDTO.<CustomerResponseDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .data(result)
                .totalItem(pageCustomer.getTotalElements())
                .totalPages(pageCustomer.getTotalPages())
                .build();
        return paginationDTO;
    }


    @Override
    public CustomerResponseDTO addCustomer(CustomerResponseDTO customerResponseDTO) {
        CustomerEntity customer=customerRepository.findByPhone(customerResponseDTO.getPhone());
        if(customer!=null){
            sendEmail.sendEmail(customerResponseDTO.getEmail(),"Mua bán nhà đất","Bạn lại muốn mua căn hộ mới!");
            return modelMapper.map(customer,CustomerResponseDTO.class);
        }
        CustomerEntity customerEntity = modelMapper.map(customerResponseDTO, CustomerEntity.class);
        customerEntity.setStatus(true);
        CustomerEntity customerEntity1=customerRepository.save(customerEntity);
        CustomerResponseDTO customerResponseDTO1=modelMapper.map(customerEntity1,CustomerResponseDTO.class);
        sendEmail.sendEmail(customerResponseDTO.getEmail(),"Mua bán nhà đất","Nhân viên tư vấn sẽ liên hệ với bạn sớm nhất có thể !");
        return customerResponseDTO1;
    }

    @Override
    public void updateCustomer(Long id, CustomerResponseDTO customerResponseDTO) {
        CustomerEntity customer1=customerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        CustomerEntity customer2=modelMapper.map(customerResponseDTO,CustomerEntity.class);
        customer2.setId(id);
        customer2.setUsers(customer1.getUsers());
        customer2.setStatus(true);
        customerRepository.save(customer2);
    }

    @Override
    public void deleteCustomer(Long id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);
        if(!customerEntity.isPresent()){
            throw new AppException(ErrorCode.USER_NOTFOUND);
        }
        CustomerEntity customer = customerEntity.get();
        customer.setStatus(false);
        customerRepository.save(customer);
    }

    @Override
    public void assignmentCustomer(AssignmentRequest assignmentRequest) {
        CustomerEntity customerEntity = customerRepository.findById(assignmentRequest.getBuildingId()).get();
        List<Long> staffId = assignmentRequest.getStaffId();
        List<UserEntity> users = new ArrayList<>();
        for(Long id : staffId){
            UserEntity user = userRepository.findById(id).get();
            users.add(user);
        }
        customerEntity.setUsers(users);
        customerRepository.save(customerEntity);
    }

    @Override
    public List<StaffResponseDTO> getInfoAssignment(Long customerId) {
        List<StaffResponseDTO> result = new ArrayList<>();
        CustomerEntity customerEntity = customerRepository.findById(customerId).get();
        List<UserEntity> users = customerEntity.getUsers();
        List<UserEntity> userEntityList=userRepository.findByRoles_Code("STAFF");
        for(UserEntity item : userEntityList){
            StaffResponseDTO staffResponseDTO =new StaffResponseDTO();
            staffResponseDTO.setFullName(item.getFullname());
            staffResponseDTO.setStaffId(item.getId());
            if(users.contains(item)){
                staffResponseDTO.setChecked(true);
            }
            else{
                staffResponseDTO.setChecked(false);
            }
            result.add(staffResponseDTO);
        }
        return result;
    }

    @Override
    public CustomerResponseDTO getInfoCustomer(Long customerId) {
        CustomerEntity customer=customerRepository.findById(customerId).get();
        CustomerResponseDTO customerResponseDTO=modelMapper.map(customer,CustomerResponseDTO.class);
        return customerResponseDTO;
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        List<CustomerEntity> customer=customerRepository.findAll();
        List<CustomerResponseDTO> customerResponseDTOS=new ArrayList<>();
        for(CustomerEntity item : customer){
            CustomerResponseDTO customerResponseDTO=modelMapper.map(item,CustomerResponseDTO.class);
            customerResponseDTOS.add(customerResponseDTO);
        }
        return customerResponseDTOS;
    }

}
