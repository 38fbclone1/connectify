package com.connectify.connectify.DTO.request;

import com.connectify.connectify.enums.EFileType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class EditFileRequest {
    private EFileType type;

    private String postId;

    private String commentId;

    private MultipartFile file;
}
