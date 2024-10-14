package com.project01.reactspring.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReponseDTO {
    private Long id;
    private Long buildingid;
    private Long customerid;
    private String code;
    private String note;
    private boolean status;
    private Date createddate;
}
