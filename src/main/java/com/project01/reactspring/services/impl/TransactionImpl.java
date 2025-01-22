package com.project01.reactspring.services.impl;

import com.project01.reactspring.dto.response.CustomerResponseDTO;
import com.project01.reactspring.dto.response.TransactionReponseDTO;
import com.project01.reactspring.entity.CustomerEntity;
import com.project01.reactspring.entity.TransactionEntity;
import com.project01.reactspring.exception.CustomException.AppException;
import com.project01.reactspring.exception.CustomException.ErrorCode;
import com.project01.reactspring.respository.BuildingRepository;
import com.project01.reactspring.respository.CustomerRepository;
import com.project01.reactspring.respository.TransactionRepository;
import com.project01.reactspring.respository.UserRepository;
import com.project01.reactspring.services.TransactionSerivces;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionImpl implements TransactionSerivces {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public List<TransactionReponseDTO> getTransactionsByCustomerId(Long id) {
        List<TransactionEntity> transactions = transactionRepository.findByCustomer_Id(id);
        List<TransactionReponseDTO> transactionsDTO = new ArrayList<>();
        for (TransactionEntity transaction : transactions) {
            TransactionReponseDTO transactionDTO = modelMapper.map(transaction, TransactionReponseDTO.class);
            transactionDTO.setBuildingid(transaction.getBuilding().getId());
            transactionDTO.setCustomerid(transaction.getCustomer().getId());
            transactionDTO.setMethod(transaction.getMethod());
            transactionsDTO.add(transactionDTO);
        }
        return transactionsDTO;
    }

    @Override
    public List<TransactionReponseDTO> getAllTransaction() {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        List<TransactionReponseDTO> transactionsDTO = new ArrayList<>();
        for (TransactionEntity transaction : transactions) {
            TransactionReponseDTO transactionDTO = modelMapper.map(transaction, TransactionReponseDTO.class);
            transactionDTO.setBuildingid(transaction.getBuilding().getId());
            transactionDTO.setCustomerid(transaction.getCustomer().getId());
            transactionDTO.setMethod(transaction.getMethod());
            transactionsDTO.add(transactionDTO);
        }
        return transactionsDTO;
    }

    @Override
    public void updateTransaction(Long id){
        TransactionEntity transaction = transactionRepository.findById(id).get();
        transaction.setStatus(true);
        transaction.setMethod("Tien mat");
        transactionRepository.save(transaction);
    }

    @Override
    public void createTransaction(TransactionReponseDTO transaction) {
        TransactionEntity transactionEntity = modelMapper.map(transaction, TransactionEntity.class);
        transactionEntity.setStatus(false);
        transactionEntity.setCustomer(customerRepository.findById(transaction.getCustomerid()).get());
        transactionEntity.setBuilding(buildingRepository.findById(transaction.getBuildingid()).get());
        transactionRepository.save(transactionEntity);
    }

    @Override
    public List<TransactionReponseDTO> getInfoTransaction(Long idCustomer) {
        List<TransactionEntity> transaction = transactionRepository.findByCustomer_Id(idCustomer);
        List<TransactionReponseDTO> transactionsDTO = new ArrayList<>();
        for (TransactionEntity transactionEntity : transaction) {
            TransactionReponseDTO transactionDTO = modelMapper.map(transactionEntity, TransactionReponseDTO.class);
            transactionDTO.setBuildingid(transactionEntity.getBuilding().getId());
            transactionDTO.setCustomerid(transactionEntity.getCustomer().getId());
            transactionDTO.setMethod(transactionEntity.getMethod());
            transactionsDTO.add(transactionDTO);
        }
        return transactionsDTO;
    }

    @Override
    public TransactionReponseDTO getTransactionsById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id).get();
        TransactionReponseDTO transactionDTO = modelMapper.map(transaction, TransactionReponseDTO.class);
        transactionDTO.setBuildingid(transaction.getBuilding().getId());
        transactionDTO.setCustomerid(transaction.getCustomer().getId());
        transactionDTO.setMethod(transaction.getMethod());
        return transactionDTO;
    }
}
