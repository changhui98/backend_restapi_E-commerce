package com.example.userservice.application.service;

import com.example.userservice.domain.entity.UserEntity;
import com.example.userservice.presentation.dto.UserDto;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    List<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String userEmail);
}