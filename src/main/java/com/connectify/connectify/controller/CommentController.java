package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.CommonWebSocketEditRequest;
import com.connectify.connectify.DTO.request.EditCommentRequest;
import com.connectify.connectify.DTO.request.SearchCommentRequest;
import com.connectify.connectify.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @MessageMapping("/posts/{postId}/comments")
    @SendTo("/posts/{postId}/comments")
    public ResponseEntity<?> create (@ModelAttribute CommonWebSocketEditRequest<EditCommentRequest, SearchCommentRequest> request) {
        System.out.println("socket");
        return commentService.create(request);
    }

    @MessageMapping("/posts")
    @SendTo("/posts")
    public String test () {
        System.out.println("socket");
        return "websocket is working!";
    }
}
