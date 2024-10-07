package com.connectify.connectify.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostRequest extends CommonSearchRequest {
    private String createdBy;
    private String group;
    private String audience;
}
