package com.connectify.connectify.DTO.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchCommentRequest extends CommonSearchRequest {
    private String createdBy;
    private String postId;
}
