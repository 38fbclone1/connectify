package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.AuthenticationRequest;
import com.connectify.connectify.DTO.request.EditAccountRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.service.AccountService;
import com.connectify.connectify.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    AccountService accountService;

    @PostMapping("/log-in")
    public ResponseEntity<?> authenticate (@RequestBody AuthenticationRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<String>> register (@RequestBody EditAccountRequest request) {
        return accountService.createAccount(request);
    }
}
