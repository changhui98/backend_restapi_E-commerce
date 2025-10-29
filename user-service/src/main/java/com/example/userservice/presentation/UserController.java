package com.example.userservice.presentation;

import com.example.userservice.application.service.UserService;
import com.example.userservice.presentation.dto.UserDto;
import com.example.userservice.presentation.dto.UserRequest;
import com.example.userservice.presentation.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-service")
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
        + ", port(server.port): " + env.getProperty("server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {

      log.info("users.welcome ip : {}, {}, {}, {}"
      ,request.getRemoteAddr(),request.getRemoteHost(), request.getRequestURI(), request.getRequestURL());

      return greeting.getMessage();
    }

    @PostMapping("/users")
    public String createUser(@RequestBody UserRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userRequest, UserDto.class);
        userService.createUser(userDto);

        return "Create user method is called";
    }
}