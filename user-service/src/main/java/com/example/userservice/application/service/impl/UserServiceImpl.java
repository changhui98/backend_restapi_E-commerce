package com.example.userservice.application.service.impl;

import com.example.userservice.application.service.UserService;
import com.example.userservice.domain.entity.UserEntity;
import com.example.userservice.infrastructure.repository.UserJpaRepository;
import com.example.userservice.presentation.dto.OrderResponse;
import com.example.userservice.presentation.dto.UserDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userJpaRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userJpaRepository.findByEmail(username).orElseThrow(
            () -> new UsernameNotFoundException(username));

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
            true, true, true, true, new ArrayList<>());
    }

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

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userJpaRepository.findByUserId(userId).orElseThrow(
            () -> new IllegalArgumentException("User not found")
        );

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        List<OrderResponse> orderList = new ArrayList<>();
        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public List<UserEntity> getUserByAll() {

        return userJpaRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String userEmail) {

        UserEntity userEntity = userJpaRepository.findByEmail(userEmail).orElseThrow(
            () -> new UsernameNotFoundException(userEmail)
        );

        return new  ModelMapper().map(userEntity, UserDto.class);
    }


}