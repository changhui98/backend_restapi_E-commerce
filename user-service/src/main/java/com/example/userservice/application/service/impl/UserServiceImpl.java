package com.example.userservice.application.service.impl;

import com.example.userservice.application.service.UserService;
import com.example.userservice.domain.entity.UserEntity;
import com.example.userservice.infrastructure.client.OrderServiceClient;
import com.example.userservice.infrastructure.repository.UserJpaRepository;
import com.example.userservice.presentation.dto.OrderResponse;
import com.example.userservice.presentation.dto.UserDto;
import feign.Feign;
import feign.FeignException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Environment env;
    private final UserJpaRepository userJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;

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

        /*
        Using a RestTemplate
         */
//        String orderUrl = String.format(env.getProperty("order-service.url"), userId);
//        ResponseEntity<List<OrderResponse>> orderListResponse =
//            restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<OrderResponse>>() {
//
//                });

        /*
        Using a FeignClient with logger
         */
//        List<OrderResponse> orderList = null;
//
//        try {
//            orderList = orderServiceClient.getOrders(userId);
//
//        } catch (FeignException e) {
//            log.error(e.getMessage());
//        }

        /*
        Using a FeignClient with ErrorDecoder
         */
        List<OrderResponse> orderList = orderServiceClient.getOrders(userDto.getUserId());
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
