package com.connectify.connectify.DTO.request;

import com.connectify.connectify.entity.Group;
import com.connectify.connectify.enums.EAudience;
import com.connectify.connectify.enums.EPostType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class EditPostRequest {
    private String id;

    private Date updatedAt;

    private String content;

    private EPostType type;

    private EAudience audience;

    private String groupId;

    private List<MultipartFile> files;
}
