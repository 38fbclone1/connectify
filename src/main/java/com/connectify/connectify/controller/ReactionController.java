package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.EditReactionRequest;
import com.connectify.connectify.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api/v1/reactions")
public class ReactionController {
    @Autowired
    ReactionService reactionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EditReactionRequest request) {
        return reactionService.create(request);
    }
}
