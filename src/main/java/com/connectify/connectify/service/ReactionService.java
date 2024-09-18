package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditReactionRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
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

import java.util.Date;
import java.util.Optional;

@Service
public class ReactionService {
    @Autowired
    ReactionRepository reactionRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    AccountService accountService;

    @Autowired
    PostRepository postRepository;

    public ResponseEntity<?> create (EditReactionRequest request) {
        Optional<Post> optionalPost = postRepository.findById(request.getPostId());
        if (optionalPost.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Reaction reaction = mapper.map(request, Reaction.class);
        reaction.setCreatedBy(accountService.getCurrentAccount());
        reaction.setCreatedAt(new Date());
        reaction.setPost(optionalPost.get());
        reactionRepository.save(reaction);
        CommonResponse<?> response = new CommonResponse<>(200, null, "React successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
