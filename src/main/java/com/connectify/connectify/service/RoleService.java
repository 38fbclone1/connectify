package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditRoleRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.entity.Permission;
import com.connectify.connectify.entity.Role;
import com.connectify.connectify.enums.EPermission;
import com.connectify.connectify.repository.PermissionRepository;
import com.connectify.connectify.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

//    @Autowired
//    PermissionRepository permissionRepository;

    @Autowired
    ModelMapper mapper;

    public ResponseEntity<?> create (EditRoleRequest request) {
        Set<EPermission> permissionNames = request.getPermissions();
//        Set<Permission> permissions = permissionRepository.findAllByName(permissionNames);
        Role newRole = new Role();
        newRole.setName(request.getName());
//        newRole.setPermissions(permissions);
        roleRepository.save(newRole);
        CommonResponse<?> response = new CommonResponse<>(200, "", "Create role successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
