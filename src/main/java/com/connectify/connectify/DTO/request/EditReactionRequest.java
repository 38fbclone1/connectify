package com.connectify.connectify.DTO.request;

import com.connectify.connectify.enums.EReactionType;
import lombok.Data;

@Data
public class EditReactionRequest {
    private String id;
    private EReactionType type;
    private String postId;
}
