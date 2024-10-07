package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.CommonWebSocketEditRequest;
import com.connectify.connectify.DTO.request.EditCommentRequest;
import com.connectify.connectify.DTO.request.EditFileRequest;
import com.connectify.connectify.DTO.request.SearchCommentRequest;
import com.connectify.connectify.DTO.response.*;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.Comment;
import com.connectify.connectify.entity.File;
import com.connectify.connectify.entity.Post;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.AccountRepository;
import com.connectify.connectify.repository.CommentRepository;
import com.connectify.connectify.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.Http;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileService fileService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthService authService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    FirebaseService firebaseService;

    public void uploadFile (Comment comment, List<MultipartFile> multipartFiles) {
        if (multipartFiles.isEmpty()) return;
        for (MultipartFile multipartFile: multipartFiles) {
            EditFileRequest editFileRequest = new EditFileRequest();
            editFileRequest.setFile(multipartFile);
            File hasUrlFile = fileService.setFileUrl(editFileRequest);
            hasUrlFile.setComment(comment);
            fileService.create(hasUrlFile);
        }
    }

    public ResponseEntity<?> create(EditCommentRequest request) throws IOException {

        Comment comment = mapper.map(request, Comment.class);

        Optional<Post> optionalPost = postRepository.findById(request.getPostId());
        if (optionalPost.isEmpty()) throw new CustomException(EError.BAD_REQUEST);

        Account currentAccount = authService.getCurrentAccount();
        comment.setCreatedBy(currentAccount);
        comment.setPost(optionalPost.get());
        Comment createdComment = commentRepository.save(comment);
        Account createdBy = createdComment.getCreatedBy();
        PublicAccountResponse publicAccountResponse = mapper.map(createdBy, PublicAccountResponse.class);
        CommentResponse commentResponse = mapper.map(createdComment, CommentResponse.class);
        commentResponse.setCreatedBy(publicAccountResponse);

        DatabaseReference databaseReference = firebaseService.getDatabaseIns("posts/");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonComment = objectMapper.writeValueAsString(commentResponse);
        databaseReference.child(request.getPostId()).child("comments").child(createdComment.getId()).setValueAsync(jsonComment);

        CommonResponse<?> response = new CommonResponse<>(200, null, "Create comment successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> update(EditCommentRequest request) {
        Optional<Comment> optionalComment = commentRepository.findById(request.getId());
        if (optionalComment.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Comment comment = optionalComment.get();
        if (!request.getContent().isEmpty()) {
            comment.setContent(request.getContent());
        }
        if (!request.getMultipartFiles().isEmpty()) {
            uploadFile(comment, request.getMultipartFiles()); // sua sau
        }
        commentRepository.save(comment);

        CommonResponse<?> response = new CommonResponse<>(200, null, "Update comment successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> search (SearchCommentRequest request) {
        List<Comment> comments = commentRepository.searchComment(request.getPage() * request.getPageSize(), request.getPageSize(), request.getSortBy(), request.getSortDir(), request.getKeyword(), request.getPostId(), request.getCreatedBy());
        List<CommentResponse> commentResponses = comments.stream().map(this::commentToCommentResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, commentResponses, "Search comments successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private CommentResponse commentToCommentResponse (Comment comment) {
        CommentResponse commentResponse = mapper.map(comment, CommentResponse.class);
        PublicAccountResponse publicAccountResponse = mapper.map(comment.getCreatedBy(), PublicAccountResponse.class);
        commentResponse.setCreatedBy(publicAccountResponse);
        return commentResponse;
    }
}
