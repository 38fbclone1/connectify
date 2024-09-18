package com.connectify.connectify.DTO.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class EditCommentRequest {
    private String id;

    private Date createdAt = new Date();

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private String postId;

    private String content;

    private List<MultipartFile> multipartFiles;
}
