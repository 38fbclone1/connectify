package com.connectify.connectify.DTO.request;

import lombok.Data;

@Data
public class CommonSearchRequest {
    private int page = 0;
    private int pageSize = 10;
    private String keyword;
    private String sortBy = "created_at";
    private String sortDir = "DESC"; // ASC | DESC
}
