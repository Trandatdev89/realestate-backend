package com.project01.reactspring.controller;

import com.project01.reactspring.dto.response.ApiResponse;
import com.project01.reactspring.dto.response.TransactionReponseDTO;
import com.project01.reactspring.services.TransactionSerivces;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionSerivces transactionSerivces;

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('STAFF')")
    public List<TransactionReponseDTO> getTransactionsByCustomerId(@PathVariable Long id) {
        List<TransactionReponseDTO> transactionReponseDTOS= transactionSerivces.getTransactionsByCustomerId(id);
        return transactionReponseDTOS;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionReponseDTO> getAllTransaction() {
        List<TransactionReponseDTO> transactionReponseDTOS= transactionSerivces.getAllTransaction();
        return transactionReponseDTOS;
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ApiResponse updateTransaction(@PathVariable Long id,@RequestBody TransactionReponseDTO transactionReponseDTO) {
        transactionSerivces.updateTransaction(id,transactionReponseDTO);
        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .build();
    }

    @PostMapping
    public ApiResponse createTransaction(@RequestBody TransactionReponseDTO transactionReponseDTO) {
        transactionSerivces.createTransaction(transactionReponseDTO);
        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .build();
    }

    @GetMapping("/check/{idCustomer}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<TransactionReponseDTO> getInfoTransaction(@PathVariable Long idCustomer) {
        List<TransactionReponseDTO> cus=transactionSerivces.getInfoTransaction(idCustomer);
        return cus;
    }

}
