package com.connectify.connectify.DTO.response;

import lombok.Data;
import java.util.Date;

@Data
public class CommentResponse {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String content;
    private PublicAccountResponse createdBy;
    private PublicAccountResponse updatedBy;
}
