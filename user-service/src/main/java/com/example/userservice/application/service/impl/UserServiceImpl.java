package com.example.userservice.application.service.impl;

import com.example.userservice.application.service.UserService;
import com.example.userservice.domain.entity.UserEntity;
import com.example.userservice.infrastructure.repository.UserJpaRepository;
import com.example.userservice.presentation.dto.UserDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userJpaRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));
        userJpaRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }



}