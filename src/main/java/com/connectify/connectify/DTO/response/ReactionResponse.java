package com.connectify.connectify.DTO.response;

import com.connectify.connectify.enums.EReactionType;
import lombok.Data;

import java.util.Date;

@Data
public class ReactionResponse {
    private String id;
    private PostResponse post;
    private PublicAccountResponse createdBy;
    private PublicAccountResponse updatedBy;
    private Date createdAt;
    private Date updateAt;
    private EReactionType type;
}
