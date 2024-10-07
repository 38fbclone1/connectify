package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditReactionRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.DTO.response.PostResponse;
import com.connectify.connectify.DTO.response.PublicAccountResponse;
import com.connectify.connectify.DTO.response.ReactionResponse;
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
import java.util.List;
import java.util.Optional;

@Service
public class ReactionService {
    @Autowired
    ReactionRepository reactionRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    PostRepository postRepository;

    @Autowired
    AuthService authService;

    public ResponseEntity<?> create (EditReactionRequest request) {
        Optional<Post> optionalPost = postRepository.findById(request.getPostId());
        if (optionalPost.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Reaction reaction = mapper.map(request, Reaction.class);
        reaction.setCreatedBy(authService.getCurrentAccount());
        reaction.setCreatedAt(new Date());
        reaction.setPost(optionalPost.get());
        Reaction createdReaction = reactionRepository.save(reaction);
        CommonResponse<?> response = new CommonResponse<>(200, createdReaction.getId(), "React successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> update (EditReactionRequest request) {
        Optional<Reaction> optionalReaction = reactionRepository.findById(request.getId());
        if (optionalReaction.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Reaction reaction = optionalReaction.get();
        if (!authService.checkIsCurrentAccount(reaction.getCreatedBy().getId())) throw new CustomException(EError.UNAUTHENTICATED);
        reaction.setType(request.getType());
        reaction.setUpdatedBy(authService.getCurrentAccount());
        reaction.setUpdatedAt(new Date());
        reactionRepository.save(reaction);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Update react successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> delete (String id) {
        reactionRepository.deleteById(id);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Delete reaction successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public List<Reaction> getReactionsByPostId (String postId) {
        return reactionRepository.findByPostId(postId);
    }

    public ReactionResponse reactionToReactionResponse(Reaction reaction) {
        ReactionResponse reactionResponse = mapper.map(reaction, ReactionResponse.class);
        PublicAccountResponse publicAccountResponse = mapper.map(reaction.getCreatedBy(), PublicAccountResponse.class);
        reactionResponse.setCreatedBy(publicAccountResponse);
        reactionResponse.setUpdatedBy(publicAccountResponse);
        Post post = reaction.getPost();
        PostResponse postResponse = mapper.map(post, PostResponse.class);
        reactionResponse.setPost(postResponse);
        return reactionResponse;
    }
}
