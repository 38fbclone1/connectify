package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins= "*")
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getAccount (@PathVariable String id) {
        return accountService.getAccount(id);
    }

}
