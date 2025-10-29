package com.example.userservice.presentation.dto;

import java.util.Date;
import lombok.Data;

@Data
public class UserDto {

    private String email;
    private String pwd;
    private String name;
    private String userId;
    private Date createAt;

    private String encryptedPwd;

}