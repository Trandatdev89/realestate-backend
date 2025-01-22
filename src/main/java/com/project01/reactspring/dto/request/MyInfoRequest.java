package com.project01.reactspring.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoRequest {
    @NotBlank(message = "Nhập tên đăng nhập mới vào field này !")
    private String username;
    @NotBlank(message = "Hãy để tên của bạn để nhận tư vấn !")
    private String fullname;
    @NotBlank(message = "Hãy để lại số điện thoại của bạn để nhận tư vấn !")
    private String address;
    private Date modifyDate;
    private MultipartFile thumnail;
}
