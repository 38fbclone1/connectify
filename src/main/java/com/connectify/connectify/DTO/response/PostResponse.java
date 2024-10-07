package com.connectify.connectify.DTO.response;

import com.connectify.connectify.entity.File;
import com.connectify.connectify.entity.Group;
import com.connectify.connectify.enums.EAudience;
import com.connectify.connectify.enums.EPostType;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class PostResponse {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String content;
    private EPostType type;
    private EAudience audience;
    private PublicAccountResponse createdBy;
    private Group group;
    private List<FileResponse> files;
    private Integer commentQuantity;
    private List<ReactionResponse> reactions;
}
