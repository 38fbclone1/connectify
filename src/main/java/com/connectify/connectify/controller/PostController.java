package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.EditPostRequest;
import com.connectify.connectify.DTO.request.SearchPostRequest;
import com.connectify.connectify.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping("")
    public ResponseEntity<?> create (@ModelAttribute EditPostRequest request) {
        return postService.create(request);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search (@RequestBody SearchPostRequest request) {
        System.out.println(request.getKeyword());
        return postService.search(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get (@PathVariable String id) {
        return postService.get(id);
    }
}
