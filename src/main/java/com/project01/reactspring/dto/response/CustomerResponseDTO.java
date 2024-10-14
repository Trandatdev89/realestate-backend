package com.project01.reactspring.dto.response;


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
public class CustomerResponseDTO {
    private Long id;

    @NotBlank(message = "Hãy để tên của bạn để nhận tư vấn !")
    private String fullname;
    private String email;

    @NotBlank(message = "Hãy để lại số điện thoại của bạn để nhận tư vấn !")
    private String phone;
    private String demand;
    private String createdby;
    private Date createddate;
    private boolean status;
    private Long staffid;
    private Long buildingid;
}
