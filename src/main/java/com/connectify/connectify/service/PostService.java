package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditFileRequest;
import com.connectify.connectify.DTO.request.EditPostRequest;
import com.connectify.connectify.DTO.request.SearchPostRequest;
import com.connectify.connectify.DTO.response.*;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.File;
import com.connectify.connectify.entity.Post;
import com.connectify.connectify.entity.Reaction;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.PostRepository;
import com.connectify.connectify.repository.ReactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    FileService fileService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    AuthService authService;

    @Autowired
    ReactionRepository reactionRepository;

    @Autowired
    ReactionService reactionService;

    public ResponseEntity<?> create (EditPostRequest request) {
        CommonResponse<String> response;
        Post post = mapper.map(request, Post.class);
        Account createdBy = authService.getCurrentAccount();
        post.setCreatedBy(createdBy);
        Post createdPost = postRepository.save(post);
        List<MultipartFile> multipartFiles = request.getFiles();

        if ((multipartFiles != null && !multipartFiles.isEmpty())) {
            for (MultipartFile multipartFile : multipartFiles) {
                EditFileRequest editFileRequest = new EditFileRequest();
                editFileRequest.setFile(multipartFile);
                File hasUrlFile = fileService.setFileUrl(editFileRequest);
                hasUrlFile.setPost(createdPost);
                fileService.create(hasUrlFile);
            }
        }
        response = new CommonResponse<>(200, "", "Create successfully!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> search (SearchPostRequest request) {
        List<Post> posts = postRepository.searchPost(request.getPage() * request.getPageSize(), request.getPageSize(), request.getSortBy(), request.getSortDir(), request.getKeyword(), request.getCreatedBy(), request.getAudience(), request.getGroup());
        List<PostResponse> postResponses = posts.stream().map(this::postToPostResponse).toList();
        PageResponse<PostResponse> page = new PageResponse<>();
        page.setRecordSize(postResponses.size());
        page.setList(postResponses);

        CommonResponse<PageResponse<PostResponse>> response = new CommonResponse<>(200, page, "Search post successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> get (String id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Post post = optionalPost.get();
        PostResponse postResponse = postToPostResponse(post);
        CommonResponse<?> response = new CommonResponse<>(200, postResponse, "Get post successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private PostResponse postToPostResponse (Post post) {
        PostResponse postResponse = mapper.map(post, PostResponse.class);
        PublicAccountResponse publicAccountResponse = mapper.map(post.getCreatedBy(), PublicAccountResponse.class);
        postResponse.setCreatedBy(publicAccountResponse);
        List<FileResponse> files = fileService.findByPostId(post.getId());
        postResponse.setFiles(files);
        List<Reaction> reactions = reactionService.getReactionsByPostId(post.getId());
        List<ReactionResponse> reactionResponses = reactions.stream().map(this::reactionToReactionResponse).toList();
        postResponse.setReactions(reactionResponses);
        return postResponse;
    }


    private ReactionResponse reactionToReactionResponse (Reaction reaction) {
        return reactionService.reactionToReactionResponse(reaction);
    }

//    private List<ReactionResponse> getReactionsByPostId (String postId) {
//        List<Reaction> reactions = reactionService.getReactionsByPostId(postId);
//        return reactions.stream().map(this::reactionToReactionResponse).toList();
//    }

}
