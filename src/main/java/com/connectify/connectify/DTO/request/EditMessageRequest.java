package com.connectify.connectify.DTO.request;

import com.connectify.connectify.DTO.response.PublicAccountResponse;
import lombok.Data;

import java.util.Date;

@Data
public class EditMessageRequest {
    private String id;
    private String messengerId;
    private PublicAccountResponse createdBy;
    private Date createdAt;
    private String content;
}
