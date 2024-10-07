package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditPermissionRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.entity.Permission;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.enums.EPermission;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.PermissionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Service
public class PermissionService {
//    @Autowired
//    ModelMapper mapper;
//
//    @Autowired
//    PermissionRepository permissionRepository;
//
//    public ResponseEntity<?> get(String id) {
//        Optional<Permission> optionalPermission = permissionRepository.findById(id);
//        if (optionalPermission.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
//        CommonResponse<?> response = new CommonResponse<>(200, optionalPermission.get(), "Get permission successfully!");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    public ResponseEntity<?> create(EditPermissionRequest request) {
//        Permission permission = mapper.map(request, Permission.class);
//        permissionRepository.save(permission);
//        CommonResponse<?> response = new CommonResponse<>(200, "", "Create permission successfully!");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    public ResponseEntity<?> update(Permission permission) {
//        permissionRepository.save(permission);
//        CommonResponse<?> response = new CommonResponse<>(200, "", "Create permission successfully!");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    public ResponseEntity<?> delete(String id) {
//        permissionRepository.deleteById(id);
//        CommonResponse<?> response = new CommonResponse<>(200, "", "Delete permission successfully!");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
