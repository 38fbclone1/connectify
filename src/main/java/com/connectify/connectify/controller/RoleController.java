package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.EditRoleRequest;
import com.connectify.connectify.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PostMapping
    public ResponseEntity<?> create (@RequestBody EditRoleRequest request) {
        return roleService.create(request);
    }
}
