package com.example.userservice.presentation.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class UserDto {

    private String email;
    private String pwd;
    private String name;
    private String userId;
    private Date createAt;

    private String encryptedPwd;
    private List<OrderResponse> orders;

}