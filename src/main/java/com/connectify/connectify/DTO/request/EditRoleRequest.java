package com.connectify.connectify.DTO.request;

import com.connectify.connectify.entity.Permission;
import com.connectify.connectify.enums.EPermission;
import com.connectify.connectify.enums.ERole;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class EditRoleRequest {
    private ERole name;
    private Set<EPermission> permissions = new HashSet();
}
