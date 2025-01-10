package com.connectify.connectify.DTO.response;

import lombok.Data;

@Data
public class GetOtpResponse {
    private int remainResent;
    private int remainRetried;
}
