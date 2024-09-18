package com.connectify.connectify.DTO.request;

import com.connectify.connectify.enums.EPermission;
import lombok.Data;

@Data
public class EditPermissionRequest {
    private EPermission name;
}
