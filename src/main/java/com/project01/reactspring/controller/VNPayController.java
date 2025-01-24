package com.project01.reactspring.controller;

import com.project01.reactspring.dto.response.VNPayResponse;
import com.project01.reactspring.entity.TransactionEntity;
import com.project01.reactspring.respository.TransactionRepository;
import com.project01.reactspring.vnpay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;


@RestController
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private TransactionRepository transactionRepository;


    @PostMapping("/payment")
    public VNPayResponse submidOrder(@RequestParam("id") int id,
                                     @RequestParam("amount") int orderTotal,
                                     @RequestParam("orderInfo") String orderInfo,
                                     HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl,id);
        return VNPayResponse.builder()
                .URL(vnpayUrl)
                .status(200L)
                .message("Success")
                .build();
    }
    @GetMapping("/vnpay-payment/{id}")
    public ResponseEntity<Void> GetMapping(@PathVariable int id, @RequestParam Map<String,String> params){
        TransactionEntity transaction = transactionRepository.findById((long)id).get();
        transaction.setStatus(true);
        transaction.setMethod("Internet banking");
        transaction.setCode(params.get("vnp_TransactionNo"));
        transaction.setNote(params.get("vnp_OrderInfo"));
        transactionRepository.save(transaction);
        String targetUrl = "https://realestate-frontend-six.vercel.app/success";
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(targetUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
