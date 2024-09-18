package com.connectify.connectify.DTO.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private PrivateAccountResponse data;
}
