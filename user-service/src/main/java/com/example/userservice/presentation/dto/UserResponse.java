package com.example.userservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String email;

    private String name;

    private String userId;

    private List<OrderResponse> orders;

}