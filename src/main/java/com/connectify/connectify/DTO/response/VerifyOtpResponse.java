package com.connectify.connectify.DTO.response;

import lombok.Data;

@Data
public class VerifyOtpResponse {
    private boolean isCorrect;
    private int remainResent;
    private int remainRetried;
    private String token;
}
