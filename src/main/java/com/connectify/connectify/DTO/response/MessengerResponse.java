package com.connectify.connectify.DTO.response;

import com.connectify.connectify.enums.EMessengerType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MessengerResponse {
    private String id;
    private List<PublicAccountResponse> members;
    private PublicAccountResponse createdBy;
    private Date createdAt;
    private EMessengerType type;
}
