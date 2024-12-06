package com.spring.backend.Response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String jwt;

    private int statusCode;

    private String message;

}
