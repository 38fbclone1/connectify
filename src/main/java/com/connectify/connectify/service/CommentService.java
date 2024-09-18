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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileService fileService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ModelMapper mapper;

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

    public ResponseEntity<?> create(CommonWebSocketEditRequest<EditCommentRequest, SearchCommentRequest> request) {
        EditCommentRequest editCommentRequest = request.getEditRequest();
        Comment comment = mapper.map(editCommentRequest, Comment.class);

        Optional<Account> optionalAccount = accountRepository.findById(editCommentRequest.getCreatedBy());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);

        Optional<Post> optionalPost = postRepository.findById(editCommentRequest.getPostId());
        if (optionalPost.isEmpty()) throw new CustomException(EError.BAD_REQUEST);

        comment.setCreatedBy(optionalAccount.get());
        comment.setPost(optionalPost.get());
        Comment newComment = commentRepository.save(comment);
//        uploadFile(newComment, editCommentRequest.getMultipartFiles());

        List<Comment> comments = search(request.getSearchRequest());
        List<CommentResponse> commentResponses = comments.stream()
                .map(a -> mapper.map(a, CommentResponse.class))
                .toList();
        PageResponse<CommentResponse> pageResponse = new PageResponse<>();
        pageResponse.setRecordSize(commentResponses.size());
        pageResponse.setList(commentResponses);

        CommonResponse<?> response = new CommonResponse<>(200, pageResponse, "Create comment successfully!");
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

    private List<Comment> search (SearchCommentRequest request) {
        return commentRepository.searchComment(request.getPage() * request.getPageSize(), request.getPageSize(), request.getSortBy(), request.getSortDir(), request.getKeyword(), request.getPostId(), request.getCreatedBy());
    }

    private CommentResponse commentToCommentResponse (Comment comment) {
        CommentResponse commentResponse = mapper.map(comment, CommentResponse.class);
        PublicAccountResponse publicAccountResponse = mapper.map(comment.getCreatedBy(), PublicAccountResponse.class);
        commentResponse.setCreatedBy(publicAccountResponse);
        return commentResponse;
    }
}
