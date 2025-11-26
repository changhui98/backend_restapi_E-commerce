package com.example.userservice.infrastructure.config;

import com.example.userservice.application.service.UserService;
import com.example.userservice.infrastructure.config.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final UserService userService;

    private final Environment env;

    private final BCryptPasswordEncoder passwordEncoder;

    public static final String ALLOWED_IP_ADDRESS = "127.0.0.1";
    public static final String SUBNET = "/32";
    public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(
        ALLOWED_IP_ADDRESS + SUBNET);

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/health-check/**").permitAll()
                .requestMatchers("/**").access(
                    new WebExpressionAuthorizationManager(
                        "hasIpAddress('127.0.0.1') or hasIpAddress('::1') or " +
                            "hasIpAddress('192.168.45.40') or hasIpAddress('::1')"))
                .anyRequest().authenticated())
            .authenticationManager(authenticationManager)
            .addFilter(getAuthenticationFilter(authenticationManager))
            .httpBasic(Customizer.withDefaults())
            .headers(h -> h.frameOptions(FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception{

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, env, authenticationManager);
        authenticationFilter.setAuthenticationManager(authenticationManager);

        return authenticationFilter;
    }

}
