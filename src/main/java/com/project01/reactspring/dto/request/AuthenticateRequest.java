package com.project01.reactspring.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateRequest {

    @NotBlank(message = "Tên đăng nhập không hợp lệ !")
    private String username;

    @NotBlank(message = "Mật khẩu sai.Vui lòng nhập lại")
    private String password;
}
