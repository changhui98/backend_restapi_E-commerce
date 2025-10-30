package com.example.userservice.application.service;

import com.example.userservice.domain.entity.UserEntity;
import com.example.userservice.presentation.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    List<UserEntity> getUserByAll();

}