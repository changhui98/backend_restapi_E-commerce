package com.example.userservice.application.service;

import com.example.userservice.presentation.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDto);

}