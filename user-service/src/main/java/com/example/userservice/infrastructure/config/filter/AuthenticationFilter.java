package com.example.userservice.infrastructure.config.filter;

import com.example.userservice.application.service.UserService;
import com.example.userservice.presentation.dto.UserDto;
import com.example.userservice.presentation.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment env;

    public AuthenticationFilter(UserService userService, Environment env,
        AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.env = env;
        super.setAuthenticationManager(authenticationManager);
    }

    public AuthenticationFilter(AuthenticationManager authenticationManager,
        UserService userService,
        Environment env) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {

            LoginRequest creds = new ObjectMapper().readValue(request.getInputStream(),
                LoginRequest.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.getEmail(),
                    creds.getPassword(),
                    new ArrayList<>()
                )
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        String userEmail = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userEmail);

        byte[] secretKeyBytes = env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8);

        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        String token = Jwts.builder()
            .subject(userDetails.getUserId())
            .expiration(Date.from(now.plusMillis(Long.parseLong(env.getProperty("token.expiration_time")))))
            .issuedAt(Date.from(now))
            .signWith(secretKey)
            .compact();


        response.addHeader("token",token);
        response.addHeader("userId", userDetails.getUserId());
    }
}