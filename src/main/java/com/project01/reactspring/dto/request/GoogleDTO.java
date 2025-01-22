package com.project01.reactspring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleDTO {
    private String googleId;
    private String username;
    private String email;
    private String thumnail;
    private String fullname ;
}
