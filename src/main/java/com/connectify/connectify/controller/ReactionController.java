package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.EditReactionRequest;
import com.connectify.connectify.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reactions")
public class ReactionController {
    @Autowired
    ReactionService reactionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EditReactionRequest request) {
        return reactionService.create(request);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody EditReactionRequest request) {
        return reactionService.update(request);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return reactionService.delete(id);
    }
}
