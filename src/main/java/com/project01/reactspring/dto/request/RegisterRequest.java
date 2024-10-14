package com.project01.reactspring.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "username thì phải 8 ký tự ")
    private String username;

    @NotBlank(message = "passowrd thì phải có 8 ký tự tro lên ")
    private String password;

    private String address;

    private String fullname;

    private Date birth;

    private String role;
}
