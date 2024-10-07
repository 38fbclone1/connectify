package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.EditMessageRequest;
import com.connectify.connectify.DTO.request.SearchMessageRequest;
import com.connectify.connectify.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/messages")
public class MessageController {
    @Autowired
    MessageService messageService;

    @PostMapping("")
    public ResponseEntity<?> create (@RequestBody EditMessageRequest request) {
        return messageService.create(request);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search (@RequestBody SearchMessageRequest request) {
        return messageService.search(request);
    }

}
