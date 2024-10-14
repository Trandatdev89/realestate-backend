package com.project01.reactspring.services;

import com.project01.reactspring.dto.response.TransactionReponseDTO;

import java.util.List;

public interface TransactionSerivces {
    List<TransactionReponseDTO> getTransactionsByCustomerId(Long id);
    List<TransactionReponseDTO> getAllTransaction();
    void updateTransaction(Long id,TransactionReponseDTO transaction);
    void createTransaction(TransactionReponseDTO transaction);
    List<TransactionReponseDTO> getInfoTransaction(Long idCustomer);
}
