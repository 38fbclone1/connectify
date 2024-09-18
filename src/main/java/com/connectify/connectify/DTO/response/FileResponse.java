package com.connectify.connectify.DTO.response;

import com.connectify.connectify.enums.EFileType;
import lombok.Data;

@Data
public class FileResponse {
    private String id;
    private String url;
    private EFileType type;
    private String postId;
}
