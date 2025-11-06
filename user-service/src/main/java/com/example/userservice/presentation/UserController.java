package com.example.userservice.presentation;

import com.example.userservice.application.service.UserService;
import com.example.userservice.domain.entity.UserEntity;
import com.example.userservice.presentation.dto.UserDto;
import com.example.userservice.presentation.dto.UserRequest;
import com.example.userservice.presentation.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final Environment env;

    private final Greeting greeting;

    private final UserService userService;

    @GetMapping("/health-check")
    public String status() {

        return String.format("It's Working in User Service"
        + ", port(local.server.port): " + env.getProperty("local.server.port")
        + ", port(server.port): " + env.getProperty("server.port")
        + ", gateway ip(env): " + env.getProperty("gateway.ip")
        + ", token secret key: " + env.getProperty("token.secret")
        + ", token expiration time: " + env.getProperty("token.expiration-time"));
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {

      log.info("users.welcome ip : {}, {}, {}, {}"
      ,request.getRemoteAddr(),request.getRemoteHost(), request.getRequestURI(), request.getRequestURL());

      return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userRequest, UserDto.class);
        userService.createUser(userDto);

        UserResponse response = mapper.map(userDto, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserEntity> userList = userService.getUserByAll();

        List<UserResponse> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, UserResponse.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        UserResponse returnValue = new ModelMapper().map(userDto, UserResponse.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
