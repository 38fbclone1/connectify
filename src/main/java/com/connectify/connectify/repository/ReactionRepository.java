package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, String> {
    List<Reaction> findByPostId(String postId);

    Optional<Reaction> findByPostIdAndCreatedBy(String post_id, Account createdBy);
}
