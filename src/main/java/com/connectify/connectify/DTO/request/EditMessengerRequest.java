package com.connectify.connectify.DTO.request;

import com.connectify.connectify.enums.EMessengerType;
import lombok.Data;

import java.util.List;

@Data
public class EditMessengerRequest {
    private String id;
    private EMessengerType type;
    private String name;
    private List<String> members;
}
